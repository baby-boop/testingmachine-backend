package testingmachine_backend.projects.meta.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import testingmachine_backend.config.SslDisableClass;
import testingmachine_backend.projects.meta.DTO.MetadataDTO;

import java.util.ArrayList;
import java.util.List;

import static testingmachine_backend.config.ConfigForAll.API_URL;

@Slf4j
public class MetaCallDataview {

    private static final String DATAVIEW = "pfFindModuleMetaLookupIdsDv";

    public static List<MetadataDTO> getProcessMetaDataList(String moduleId, String unitName, String systemUrl, String username, String password) {

        SslDisableClass.SslDisabler();

        String SERVICE_URL =  systemUrl + API_URL;

        List<MetadataDTO> metaList = new ArrayList<>();
        String payload = """
            {
                "username": "%s",
                "password": "%s",
                "command": "PL_MDVIEW_005",
                "unitname": "%s",
                "parameters": {
                    "systemmetagroupcode": "%s",
                    "filtermoduleid": "%s"
                }
            }
            """.formatted(username, password, unitName, DATAVIEW, moduleId);
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(SERVICE_URL, HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode resultNode = rootNode.path("response").path("result");

            if (resultNode.isObject()) {
                resultNode.fields().forEachRemaining(entry -> {
                    JsonNode item = entry.getValue();

                    String id = item.path("metadataid").asText("N/A");
                    String moduleName = item.path("modulename").asText("N/A");
                    String code = item.path("metadatacode").asText("N/A");
                    String name = item.path("metadataname").asText("N/A");

                    metaList.add(new MetadataDTO(id, moduleName, code, name, ""));

                });
            } else {
                log.warn("The 'result' node is missing or not an object.");
            }

        } catch (Exception e) {
            log.error("Error while calling service: ", e);
        }
        return metaList;
    }
}

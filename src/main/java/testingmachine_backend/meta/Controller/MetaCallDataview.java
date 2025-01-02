package testingmachine_backend.meta.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import testingmachine_backend.config.SslDisableClass;
import testingmachine_backend.meta.DTO.MetadataDTO;
import testingmachine_backend.controller.JsonController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MetaCallDataview {

    //    private static final String http = "http://";
//    static String HOST = JsonController.getSystemURL();
//    private static final String PORT = "8080";
//    private static final String URL = "/erp-services/RestWS/runJson";
//    private static final String SERVICE_URL = http + HOST + ":" + PORT + URL;
//    private static final String DATAVIEW = "pfFindModuleMetaLookupIdsDv";

    private static final String HOST =  "http://" + JsonController.getSystemURL();
//    private static final String HOST = JsonController.getSystemURL();
    private static final String PORT = ":8080";
//    private static final String URL = "/javarestapi";
    private static final String URL = "/erp-services/RestWS/runJson";
    private static final String SERVICE_URL =  HOST + PORT + URL;
    private static final String DATAVIEW = "pfFindModuleMetaLookupIdsDv";
    public static List<MetadataDTO> getProcessMetaDataList() {

        SslDisableClass.SslDisabler();
        String moduleId = JsonController.getModuleId();
        String USERNAME = JsonController.getUsername();
        String PASSWORD = JsonController.getPassword();
        String UNITNAME = JsonController.getDatabaseUsername();

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
            """.formatted(USERNAME, PASSWORD, UNITNAME, DATAVIEW, moduleId);
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

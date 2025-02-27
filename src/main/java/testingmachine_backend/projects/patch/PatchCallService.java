package testingmachine_backend.projects.patch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PatchCallService {

    private static final String PORT = "8080";
    private static final String URL = "/erp-services/RestWS/runJson";
    private static final String DATAVIEW = "pfFindPatchMetaIdsDv";

    public static List<PatchDTO> getPatchMetaDataList(String unitName, String systemUrl, String username, String password, String patchId) {

        String SERVICE_URL =  systemUrl + ":" + PORT + URL;

        List<PatchDTO> patchList = new ArrayList<>();

        String payload = """
            {
                "username": "%s",
                "password": "%s",
                "command": "PL_MDVIEW_005",
                "unitname": "%s",
                "parameters": {
                    "systemmetagroupcode": "%s",
                    "patchId": "%s"
                }
            }
            """.formatted(username, password, unitName, DATAVIEW, patchId);

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

                    String id = item.path("metaid").asText("N/A");
                    String code = item.path("metacode").asText("N/A");
                    String name = item.path("metaname").asText("N/A");
                    String typeId = item.path("typeid").asText("N/A");
                    String patchName = item.path("patchname").asText("N/A");

                    patchList.add(new PatchDTO(id, code, name, typeId, patchId, patchName));
                });
            } else {
                log.warn("The 'result' node is missing or not an object.");
            }

        } catch (Exception e) {
            log.error("Error while calling service: ", e);
        }

        return patchList;
    }
}

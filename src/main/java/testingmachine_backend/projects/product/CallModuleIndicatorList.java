package testingmachine_backend.projects.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

@Slf4j
public class CallModuleIndicatorList {

    public static List<MetadataDTO> getIndicatorList(String moduleId, String username, String password) {

        SslDisableClass.SslDisabler();

        String SERVICE_URL =  "https://dev.veritech.mn/restapi" ;

        List<MetadataDTO> metaList = new ArrayList<>();
        String payload = """
            {
                "request": {
                    "username": "batdelger",
                    "password": "123",
                    "command": "kpiIndicatorDataList",
                    "parameters": {
                        "indicatorId": "17434887479421"
                    }
                }
            }
            """.formatted(username, password, moduleId);

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(SERVICE_URL, HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = response.getBody();
            if (json != null && json.startsWith("\uFEFF")) {
                json = json.substring(1);
            }

            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode resultNode = rootNode.path("response").path("result").path("rows");
            if (resultNode.isArray()) {
                for (JsonNode item : resultNode) {
                    String id = item.path("ID").asText("N/A");
                    metaList.add(new MetadataDTO(id, "", "", "", ""));
                }
            }

        } catch (Exception e) {
            log.error("Error while calling service: ", e);
        }
        return metaList;
    }
}

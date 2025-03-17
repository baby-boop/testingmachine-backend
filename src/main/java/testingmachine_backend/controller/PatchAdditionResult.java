package testingmachine_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import testingmachine_backend.config.SslDisableClass;


import static testingmachine_backend.config.ConfigForAll.PROCESS_URL;

@Slf4j
public class PatchAdditionResult {
    private static final String DATAVIEW = "testingPatchProcess_001";

    public static void getProcessMetaDataList( String unitName, String systemUrl, String username, String password, String fullUrl, String patchId, int isSuccessTest) {

        SslDisableClass.SslDisabler();

        String payload = """
                {
                    "request": {
                        "username": "%s",
                        "password": "%s",
                        "command": "%s",
                        "unitname": "%s",
                        "parameters": {
                            "physicalpath": "%s",
                            "relatedid": "%s",
                            "issuccesstest": "%s"
                        }
                    }
                }
            """.formatted(username, password, DATAVIEW, unitName, fullUrl, patchId, isSuccessTest);
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(PROCESS_URL, HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            System.out.println("rootNode: " + rootNode);

        } catch (Exception e) {
            log.error("Error while calling service: ", e);
        }
    }
}

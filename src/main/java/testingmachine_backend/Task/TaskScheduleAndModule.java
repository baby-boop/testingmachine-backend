package testingmachine_backend.Task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static testingmachine_backend.config.ConfigForAll.getLocalIpAddress;
import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

@Component
@Slf4j
public class TaskScheduleAndModule {

     @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 500000)
    public void processTasks() {
        processTaskFiles();
    }

    private void processTaskFiles() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        mapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));

        File folder = new File(BASE_DIRECTORY + "/schedule");

        if (folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".json")))) {
                try {
                    List<TaskDTO> tasksList = mapper.readValue(file, new TypeReference<>() {
                    });

                    for (TaskDTO task : tasksList) {
                        if (task.isDue()) {
                            System.out.println("Хугацаа нь болсон: " + task.getTaskName() + " - " + task.getModuleId());
                            task.setLastUpdatedDate(java.time.LocalDate.now());
                            setModuleWithProcessList(task.getModuleId(), task.getTaskName());
                        }
//                        else {
//                            System.out.println("Хугацаа нь болоогүй: " + task.getTaskName() + " - " + task.getModuleId());
//                        }
                    }

                    mapper.writeValue(file, tasksList);
                    System.out.println("Updated json file: " + file.getName());

                } catch (Exception e) {
                    System.err.println("Error processing task " + file.getName() + ": " + e.getMessage());
                }
            }
        } else {
            System.err.println("Invalid folder path: " + "/opt/app/json_data/schedule");
        }
    }


    public static void setModuleWithProcessList(String moduleId, String moduleName) {
//        String SERVICE_URL = "http://172.169.88.222:8282/system-data";
        String localIpAddress = getLocalIpAddress();
        String SERVICE_URL = "http://"+ localIpAddress +":8282/system-data";

        String payload = """
            {
                "moduleId": "%s",
                "username": "batdelger",
                "password": "123",
                "customerName": "%s",
                "databaseName": "",
                "databaseUsername": "",
                "selectedModule": "process",
                "systemURL": "http://dev.veritech.mn",
                "metaProcessId": ""
            }
        """.formatted(moduleId, moduleName);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(SERVICE_URL, HttpMethod.POST, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

        } catch (Exception e) {
            log.error("Error while calling service: ", e);
        }
    }


}

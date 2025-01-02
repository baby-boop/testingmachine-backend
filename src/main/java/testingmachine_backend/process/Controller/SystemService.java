package testingmachine_backend.process.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

@Service
public class SystemService {

    private String determineDirectoryPath(String selectedModule) {
        if ("process".equalsIgnoreCase(selectedModule)) {
            return BASE_DIRECTORY + "/process/header";
        } else if ("meta".equalsIgnoreCase(selectedModule)) {
            return BASE_DIRECTORY + "/metalist/header";
        } else {
            return BASE_DIRECTORY + "/metalistwithprocess/header";
        }
    }

    public List<SystemData> getAllSystemData(String selectedModule) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<SystemData> allSystemData = new ArrayList<>();

        String directoryPath = determineDirectoryPath(selectedModule);
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith("_header.json"));

        if (files != null) {
            for (File file : files) {
                try {
                    SystemData data = objectMapper.readValue(file, SystemData.class);
                    allSystemData.add(data);
                } catch (IOException e) {
                    e.printStackTrace(System.out); // Log errors to the console
                }
            }
        }

        return allSystemData;
    }

    public SystemData addSystemData(SystemData data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String directoryPath = determineDirectoryPath(data.getSelectedModule());
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Ensure the directory exists
            }

            String generatedId = UUID.randomUUID().toString();
            File file = new File(directoryPath + File.separator + generatedId + "_header.json");
            data.setGeneratedId(generatedId);
            objectMapper.writeValue(file, data);

            return data;
        } catch (IOException e) {
            e.printStackTrace(System.out); // Log errors to the console
            return null;
        }
    }
}

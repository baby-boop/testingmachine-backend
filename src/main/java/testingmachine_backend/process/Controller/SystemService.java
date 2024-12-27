package testingmachine_backend.process.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SystemService {

    private static final String DIRECTORY_PATH1 = "src/main/java/testingmachine_backend/json/process/header";
    private static final String DIRECTORY_PATH2 = "src/main/java/testingmachine_backend/json/metalist/header";
    private static final String DIRECTORY_PATH3 = "src/main/java/testingmachine_backend/json/metalistwithprocess/header";

    private String determineDirectoryPath(String selectedModule) {
        if ("process".equalsIgnoreCase(selectedModule)) {
            return DIRECTORY_PATH1;
        } else if ("meta".equalsIgnoreCase(selectedModule)) {
            return DIRECTORY_PATH2;
        } else {
            return DIRECTORY_PATH3;
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
                    e.printStackTrace(
                            System.out
                    );
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
                directory.mkdirs();
            }

            String generatedId = UUID.randomUUID().toString();
            File file = new File(directoryPath + File.separator + generatedId + "_header.json");
            data.setGeneratedId(generatedId);
            objectMapper.writeValue(file, data);

            return data;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
}

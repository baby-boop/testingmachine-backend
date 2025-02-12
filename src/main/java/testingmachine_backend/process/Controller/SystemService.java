package testingmachine_backend.process.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;
import static testingmachine_backend.controller.JsonController.generateProcessData;

@Service
public class SystemService {

    private String determineDirectoryPath(String selectedModule, String metaOrPatchId) {
        if ("process".equals(selectedModule) && metaOrPatchId != null) {
            return BASE_DIRECTORY + "/nodatas";
        }
        else if ("process".equalsIgnoreCase(selectedModule) && metaOrPatchId == null) {
            return BASE_DIRECTORY + "/process/header";
        } else if ("meta".equalsIgnoreCase(selectedModule)) {
            return BASE_DIRECTORY + "/metalist/header";
        } else if ("patch".equalsIgnoreCase(selectedModule)) {
            return BASE_DIRECTORY + "/patch/header";
        } else if ("metalistwithprocess".equalsIgnoreCase(selectedModule)) {
            return BASE_DIRECTORY + "/metalistwithprocess/header";
        } else if ("metaverse".equalsIgnoreCase(selectedModule)){
            return BASE_DIRECTORY + "/metaverse/header";
        } else if ("indicator".equalsIgnoreCase(selectedModule)){
            return BASE_DIRECTORY + "/indicator/header";
        }
            else {
            return BASE_DIRECTORY + "/default";
        }
    }

    //Бүх файлыг унших
//    public List<SystemData> getAllSystemData(String selectedModule) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<SystemData> allSystemData = new ArrayList<>();
//
//        String directoryPath = determineDirectoryPath(selectedModule);
//        File directory = new File(directoryPath);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        File[] files = directory.listFiles((dir, name) -> name.endsWith("_header.json"));
//
//        if (files != null) {
//            for (File file : files) {
//                try {
//                    SystemData data = objectMapper.readValue(file, SystemData.class);
//                    allSystemData.add(data);
//                } catch (IOException e) {
//                    e.printStackTrace(System.out); // Log errors to the console
//                }
//            }
//        }
//
//        return allSystemData;
//    }

    public SystemData addSystemData(SystemData data) {
        ObjectMapper objectMapper = new ObjectMapper();
        String generatedId = UUID.randomUUID().toString();
        data.setGeneratedId(generatedId);

        // `metaOrPatchId` нь `null` биш бол файл хадгалахгүй, зөвхөн `generatedId` үүсгэнэ.
        if ("process".equals(data.getSelectedModule()) && data.getMetaOrPatchId() != null) {
            return data;
        }

        try {
            String directoryPath = determineDirectoryPath(data.getSelectedModule(), data.getMetaOrPatchId());
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryPath + File.separator + generatedId + "_header.json");
            objectMapper.writeValue(file, data);

            return data;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
}

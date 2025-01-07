package testingmachine_backend.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import testingmachine_backend.controller.JsonController;

import java.io.File;

@Component
public class JsonFileCleaner {

    private static final String[] DIRECTORIES_TO_CLEAN = {
            JsonController.BASE_DIRECTORY + "/metalist/header",
            JsonController.BASE_DIRECTORY + "/metalist/result",
            JsonController.BASE_DIRECTORY + "/process/header",
            JsonController.BASE_DIRECTORY + "/process/result",
            JsonController.BASE_DIRECTORY + "/metalistwithprocess/header",
            JsonController.BASE_DIRECTORY + "/metalistwithprocess/result"
    };

//    @Scheduled(cron = "0 0 0 L * ?")
    @Scheduled(fixedRate = 600_000)
    public void cleanJsonFiles() {
        for (String directoryPath : DIRECTORIES_TO_CLEAN) {
            File folder = new File(directoryPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
                if (files != null) {
                    for (File file : files) {
                        if (file.delete()) {
                            System.out.println("Deleted: " + file.getAbsolutePath());
                        } else {
                            System.out.println("Failed to delete: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}

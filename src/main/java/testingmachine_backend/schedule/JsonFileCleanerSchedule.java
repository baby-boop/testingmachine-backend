package testingmachine_backend.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import testingmachine_backend.controller.JsonController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Component
@Slf4j
public class JsonFileCleanerSchedule {

    private static final long MAX_FILE_AGE_DAYS = 14;
    private static final long MAX_FILE_AGE_MINUTES = 5;

    public static void allClearData() {
        String[] directories = {
                JsonController.BASE_DIRECTORY + "/metaverse/header",
                JsonController.BASE_DIRECTORY + "/metaverse/result",
                JsonController.BASE_DIRECTORY + "/indicator/header",
                JsonController.BASE_DIRECTORY + "/indicator/result"
        };

        for (String dirPath : directories) {
            File folder = new File(dirPath);

            if (folder.exists() && folder.isDirectory()) {
                File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

                if (jsonFiles != null) {
                    for (File jsonFile : jsonFiles) {
                        boolean deleted = jsonFile.delete();
                        if (!deleted) {
                            System.err.println("Failed to delete file: " + jsonFile.getAbsolutePath());
                        }
                    }
                }
            } else {
                System.err.println("Directory does not exist or is not a directory: " + dirPath);
            }
        }
    }
    //    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanAllJsonDirectories() {
        String[] directories = {
                JsonController.BASE_DIRECTORY + "/metalist/header",
                JsonController.BASE_DIRECTORY + "/metalist/result",
                JsonController.BASE_DIRECTORY + "/process/header",
                JsonController.BASE_DIRECTORY + "/process/result",
                JsonController.BASE_DIRECTORY + "/patch/header",
                JsonController.BASE_DIRECTORY + "/patch/result",
                JsonController.BASE_DIRECTORY + "/metalistwithprocess/header",
                JsonController.BASE_DIRECTORY + "/metalistwithprocess/result",
                JsonController.BASE_DIRECTORY + "/percent/header",
                JsonController.BASE_DIRECTORY + "/percent/result"
        };

        for (String folderPath : directories) {
//            cleanUpOldMinuteFiles(folderPath);
            cleanUpOldDaysFiles(folderPath);
        }
    }

    //Минутаар тооцоолж устгах
    private void cleanUpOldMinuteFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    Path filePath = jsonFile.toPath();
                    Instant creationTime = Files.readAttributes(filePath, BasicFileAttributes.class).creationTime().toInstant();
                    long fileAgeInMinutes = Duration.between(creationTime, Instant.now()).toMinutes();

                    if (fileAgeInMinutes > MAX_FILE_AGE_MINUTES) {
                        boolean deleted = jsonFile.delete();
                        if (deleted) {
                            log.info("Deleted old JSON file: {}", jsonFile.getName());
                        } else {
                            log.warn("Failed to delete file: {}", jsonFile.getName());
                        }
                    }
                } catch (IOException e) {
                    log.error("Error reading file attributes for: {}", jsonFile.getName(), e);
                }
            }
        }
    }

    //Өдрөөр тооцоолж устгах
    private void cleanUpOldDaysFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    Path filePath = jsonFile.toPath();
                    Instant creationTime = Files.readAttributes(filePath, BasicFileAttributes.class).creationTime().toInstant();
                    long fileAgeInDays = Duration.between(creationTime, Instant.now()).toDays();

                    // 14 хоногоос дээш настай файлуудыг устгах
                    if (fileAgeInDays > MAX_FILE_AGE_DAYS) {
                        boolean deleted = jsonFile.delete();
                        if (deleted) {
                            log.info("Deleted old JSON file: {}", jsonFile.getName());
                        } else {
                            log.warn("Failed to delete file: {}", jsonFile.getName());
                        }
                    }
                } catch (IOException e) {
                    log.error("Error reading file attributes for: {}", jsonFile.getName(), e);
                }
            }
        }
    }
}

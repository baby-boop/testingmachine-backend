package testingmachine_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testingmachine_backend.config.ThreadDumpService;
import testingmachine_backend.projects.process.Config.ConfigProcess;
import testingmachine_backend.projects.process.Controller.FileData;
import testingmachine_backend.projects.process.Controller.SystemData;
import testingmachine_backend.projects.process.Controller.SystemService;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class JsonController {

    public static final String BASE_DIRECTORY = "/opt/app/json_data";
//    public static final String BASE_DIRECTORY = "src/main/java/testingmachine_backend/json"; //Өөрийн датаг хадгалах
    private static final String META_HEADER_PATH = BASE_DIRECTORY + "/metalist/header";
    private static final String META_RESULT_PATH = BASE_DIRECTORY + "/metalist/result";
    private static final String PROCESS_HEADER_PATH = BASE_DIRECTORY + "/process/header";
    private static final String PROCESS_RESULT_PATH = BASE_DIRECTORY + "/process/result";
    private static final String META_PROCESS_HEADER_PATH = BASE_DIRECTORY + "/metalistwithprocess/header";
    private static final String META_PROCESS_RESULT_PATH = BASE_DIRECTORY + "/metalistwithprocess/result";
    private static final String PATCH_HEADER_PATH = BASE_DIRECTORY + "/patch/header";
    private static final String PATCH_RESULT_PATH = BASE_DIRECTORY + "/patch/result";
    private static final String DIRECTORY_PERCENT_HEADER = BASE_DIRECTORY + "/percent/header";
    private static final String DIRECTORY_PERCENT_RESULT = BASE_DIRECTORY + "/percent/result";
    private static final String DIRECTORY_DEFAULT = BASE_DIRECTORY + "/default";

    private static String moduleId;
    private static String customerName;
    private static String createdDate;
    private static String systemURL;
    private static String username;
    private static String password;
    private static String databaseName;
    private static String databaseUsername;
    private static String jsonId;
    private static String selectedModule;
    private static String metaOrPatchId;
    private static String isCheckBox;

    public static String getModuleId() {
        return moduleId != null ? moduleId : "";
    }

    public static String getMetaOrPatchId() {
        return metaOrPatchId != null ? metaOrPatchId : "";
    }

    public static String getCustomerName() {
        return customerName != null ? customerName : "";
    }

    public static String getCreatedDate() {
        return createdDate != null ? createdDate : "";
    }

    public static String getSystemURL() {
        return systemURL != null ? systemURL : "";
    }

    public static String getUsername() {
        return username != null ? username : "";
    }

    public static String getPassword() {
        return password != null ? password : "";
    }

    public static String getDatabaseName() {
        return databaseName != null ? databaseName : "";
    }

    public static String getDatabaseUsername() {
        return databaseUsername != null ? databaseUsername : "";
    }

    public static String getJsonId() {
        return jsonId != null ? jsonId : "";
    }

    public static String getSelectedModule() {
        return selectedModule != null ? selectedModule : "";
    }

    public static String getIsCheckBox() {
        return isCheckBox != null ? isCheckBox : "";
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public SystemService service;

    private static void clearStaticData() {
        moduleId = "";
        customerName = "";
        createdDate = "";
        systemURL = "";
        username = "";
        password = "";
        databaseName = "";
        databaseUsername = "";
        jsonId = "";
        selectedModule = "";
        metaOrPatchId = "";
        isCheckBox = "";
    }

    @Autowired
    private ModuleExecutionService moduleExecutionService;

    @PostMapping("/system-data")
    public ResponseEntity<Map<String, Object>> addSystemData(@RequestBody SystemData data) {
        clearStaticData();
//        JsonFileCleanerSchedule.allClearData();
        SystemData savedData = service.addSystemData(data);
        updateStaticData(savedData);
        log.info("Created system data: ID = {}, databaseName = {}", savedData.getGeneratedId(), savedData.getDatabaseName());

        String jsonId = savedData.getGeneratedId();

        CompletableFuture<Void> resultFuture = moduleExecutionService.executeModuleAsync(data.getSelectedModule(), savedData)
                .thenAccept(result -> log.info("Амжилттай ажиллав: {}", result))
                .exceptionally(e -> {
                    log.error("Модуль ажиллуулахад алдаа: {}", e.getMessage(), e);
                    return null;
                });

        Map<String, Object> resultResponse = new HashMap<>();
        resultResponse.put("message", "Тестийг амжилттай хүлээж авлаа");

        return ResponseEntity.status(HttpStatus.CREATED).body(resultResponse);
    }

    private void updateStaticData(SystemData data) {
        moduleId = data.getModuleId();
        customerName = data.getCustomerName();
        createdDate = ConfigProcess.DateUtils.getCurrentDateTime();
        systemURL = data.getSystemURL();
        username = data.getUsername();
        password = data.getPassword();
        databaseName = data.getDatabaseName();
        databaseUsername = data.getDatabaseUsername();
        jsonId = data.getGeneratedId();
        selectedModule = data.getSelectedModule();
        metaOrPatchId = data.getMetaOrPatchId();
        isCheckBox = data.getIsCheckBox();
    }

    @GetMapping("/result/{jsonId}")
    public ResponseEntity<FileData> getResultDataByJsonId(@PathVariable String jsonId) {
        List<String> searchPaths = List.of(
                META_PROCESS_RESULT_PATH,
                DIRECTORY_PERCENT_RESULT,
                PROCESS_RESULT_PATH,
                META_RESULT_PATH,
                PATCH_RESULT_PATH
        );

        for (String path : searchPaths) {
            File folder = new File(path);
            File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

            if (jsonFiles != null) {
                for (File jsonFile : jsonFiles) {
                    String id = jsonFile.getName();
                    if (id.startsWith(jsonId)) {
                        try {
                            Object data = objectMapper.readValue(jsonFile, Object.class);
                            if (id.endsWith("_result.json")) {
                                id = id.substring(0, id.length() - "_result.json".length());
                            }
                            return ResponseEntity.ok(new FileData(id, data));
                        } catch (IOException e) {
                            e.printStackTrace(System.out);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping("/process-header")
    public ResponseEntity<List<Object>> getHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(PROCESS_HEADER_PATH));
    }

    /** Thread мэдээллийг харах
     */
    @GetMapping("/thread-status")
    public Map<String, Object> checkThreadStatus() {
        return ThreadDumpService.getActiveAndWaitingThreads();
    }

    @GetMapping("/process-result")
    public ResponseEntity<List<FileData>> getResultData() {
        return ResponseEntity.ok(readJsonFilesFromFolderResult(PROCESS_RESULT_PATH));
    }

    @GetMapping("/meta-header")
    public ResponseEntity<List<Object>> getMetaHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(META_HEADER_PATH));
    }

    @GetMapping("/meta-result")
    public ResponseEntity<List<FileData>> getMetaResultData() {
        return ResponseEntity.ok(readJsonFilesFromFolderResult(META_RESULT_PATH));
    }

    @GetMapping("/metaprocess-header")
    public ResponseEntity<List<Object>> getMetaProcessHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(META_PROCESS_HEADER_PATH));
    }

    @GetMapping("/metaprocess-result")
    public ResponseEntity<List<FileData>> getMetaProcessResultData() {
        return ResponseEntity.ok(readJsonFilesFromFolderResult(META_PROCESS_RESULT_PATH));
    }

    @GetMapping("/patch-header")
    public ResponseEntity<List<Object>> getPatchHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(PATCH_HEADER_PATH));
    }

    @GetMapping("/patch-result")
    public ResponseEntity<List<FileData>> getPatchResultData() {
        return ResponseEntity.ok(readJsonFilesFromFolderResult(PATCH_RESULT_PATH));
    }

    @GetMapping("/percent-header")
    public ResponseEntity<List<Object>> getPercentHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(DIRECTORY_PERCENT_HEADER));
    }

    @GetMapping("/percent-result")
    public ResponseEntity<List<FileData>> getPercentResultData() {
        return ResponseEntity.ok(readJsonFilesFromFolderResult(DIRECTORY_PERCENT_RESULT));
    }

    private List<Object> readJsonFilesFromFolder(String folderPath) {
        List<Object> jsonDataList = new ArrayList<>();
        File folder = new File(folderPath);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    Object data = objectMapper.readValue(jsonFile, Object.class);
                    jsonDataList.add(data);
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return jsonDataList;
    }

    private List<FileData> readJsonFilesFromFolderResult(String folderPath) {
        List<FileData> fileDataList = new ArrayList<>();
        File folder = new File(folderPath);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    String fileName = jsonFile.getName();
                    Object data = objectMapper.readValue(jsonFile, Object.class);
                    if (fileName.endsWith("_result.json")) {
                        fileName = fileName.substring(0, fileName.length() - "_result.json".length());
                    }
                    fileDataList.add(new FileData(fileName, data));
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return fileDataList;
    }
}

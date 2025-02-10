package testingmachine_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testingmachine_backend.TestingmachineBackendApplication;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.FileData;
import testingmachine_backend.process.Controller.SystemData;
import testingmachine_backend.process.Controller.SystemService;
import testingmachine_backend.process.DTO.ProcessDTO;
import testingmachine_backend.process.Service.ProcessService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class JsonController {

    public static final String BASE_DIRECTORY = "/opt/app/json_data";
//    public static final String BASE_DIRECTORY = "src/main/java/testingmachine_backend/json";
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
    private static final String NO_DATA = BASE_DIRECTORY + "/nodata";

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



    public static List<ProcessDTO> generateProcessData() {
        List<ProcessDTO> processData = new ArrayList<>();

        processData.add(new ProcessDTO("101", 10, 4, "Customer A", "2025-01-09 09:15:30", "a123b456-c789-1234-d567-890e12fgh345", "module1", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("102", 8, 8, "Customer B", "2025-01-08 11:05:45", "b234c567-d890-2345-e678-901f23g456hi", "module2", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("103", 15, 7, "Customer C", "2025-01-07 14:22:10", "c345d678-e901-3456-f789-012g34h567ij", "module3", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("104", 20, 18, "Customer D", "2025-01-06 08:30:20", "d456e789-f012-4567-g890-123h45i678jk", "module4", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("105", 12, 5, "Customer E", "2025-01-05 16:45:50", "e567f890-g123-5678-h901-234i56j789kl", "module5", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("106", 6, 6, "Customer F", "2025-01-04 19:10:40", "f678g901-h234-6789-i012-345j67k890lm", "module6", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("107", 5, 1, "Customer G", "2025-01-03 13:55:15", "g789h012-i345-7890-j123-456k78l901mn", "module7", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("108", 14, 11, "Customer H", "2025-01-02 10:05:25", "h890i123-j456-8901-k234-567l89m012no", "module8", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("109", 9, 4, "Customer I", "2025-01-01 18:20:35", "i901j234-k567-9012-l345-678m90n123op", "module9", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("110", 11, 10, "Customer J", "2024-12-31 21:40:10", "j012k345-l678-0123-m456-789n01o234pq", "module10", "http://dev.veritech.mn"));

        processData.add(new ProcessDTO("111", 7, 2, "Customer K", "2025-01-09 10:10:10", "k123l456-m789-1234-n567-890o12pqrstu", "module11", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("112", 16, 14, "Customer L", "2025-01-08 13:15:30", "l234m567-n890-2345-o678-901p23qrstuv", "module12", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("113", 18, 17, "Customer M", "2025-01-07 15:45:00", "m345n678-o901-3456-p789-012q34rstuvw", "module13", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("114", 13, 6, "Customer N", "2025-01-06 09:25:20", "n456o789-p012-4567-q890-123r45stuvwx", "module14", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("115", 10, 5, "Customer O", "2025-01-05 12:35:45", "o567p890-q123-5678-r901-234s56tuvwxyz", "module15", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("116", 22, 20, "Customer P", "2025-01-04 14:50:30", "p678q901-r234-6789-s012-345t67uvwxyz1", "module16", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("117", 4, 3, "Customer Q", "2025-01-03 16:40:25", "q789r012-s345-7890-t123-456u78vwxyz12", "module17", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("118", 19, 15, "Customer R", "2025-01-02 11:30:40", "r890s123-t456-8901-u234-567v89wxyz123", "module18", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("119", 12, 10, "Customer S", "2025-01-01 17:20:50", "s901t234-u567-9012-v345-678w90xyz1234", "module19", "http://dev.veritech.mn"));
        processData.add(new ProcessDTO("120", 25, 21, "Customer T", "2024-12-31 20:10:15", "t012u345-v678-0123-w456-789x01yz12345", "module20", "http://dev.veritech.mn"));

        return processData;

    }


    @GetMapping("/process-percent")
    public ResponseEntity<List<ProcessDTO>> getProcessPercent() {
//        List<ProcessDTO> processResults = ProcessService.getInstance().getProcessResults();
        List<ProcessDTO> processResults = generateProcessData();
        if (processResults.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(processResults);
    }

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
    }


    @GetMapping("/process-result/{jsonId}")
    public ResponseEntity<FileData> getResultDataByJsonId(@PathVariable String jsonId) {
        File folder = new File(NO_DATA);
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    @Autowired
    private ModuleExecutionService moduleExecutionService;

    @PostMapping("/system-data")
    public ResponseEntity<SystemData> addSystemData(@RequestBody SystemData data) {
        clearStaticData();
        SystemData savedData = service.addSystemData(data);
        updateStaticData(savedData);

        log.info("Created system data: ID = {}, databaseName = {}, databaseUsername = {}, ModuleId = {}, CustomerName = {}, SystemURL = {}, username = {}, password = {}, selectedModule = {}, metaProcessId = {}",
                savedData.getGeneratedId(), savedData.getDatabaseName(), savedData.getDatabaseUsername(), savedData.getModuleId(), savedData.getCustomerName(),
                savedData.getSystemURL(), savedData.getUsername(), savedData.getPassword(), savedData.getSelectedModule(), savedData.getMetaOrPatchId());

        String module = data.getSelectedModule();
        CompletableFuture<Void> resultFuture = moduleExecutionService.executeModuleAsync(module, savedData)
                .thenAccept(result -> log.info("Амжилттай ажиллав: {}", result))
                .exceptionally(e -> {
                    log.error("Модуль ажиллуулахад алдаа: {}", e.getMessage(), e);
                    return null;
                });

        resultFuture.join();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
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
    }

    private final ObjectMapper objectMapper = new ObjectMapper();



    @GetMapping("/process-header")
    public ResponseEntity<List<Object>> getHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(PROCESS_HEADER_PATH));
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

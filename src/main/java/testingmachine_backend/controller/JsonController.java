package testingmachine_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.FileData;
import testingmachine_backend.process.Controller.SystemData;
import testingmachine_backend.process.Controller.SystemService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class JsonController {

    private static final String PROCESS_HEADER_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\main\\java\\testingmachine_backend\\json\\process\\header";
    private static final String PROCESS_RESULT_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\main\\java\\testingmachine_backend\\json\\process\\result";
    private static final String META_HEADER_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\main\\java\\testingmachine_backend\\json\\metalist\\header";
    private static final String META_RESULT_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\main\\java\\testingmachine_backend\\json\\metalist\\result";
    private static final String META_PROCESS_HEADER_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\main\\java\\testingmachine_backend\\json\\metalistwithprocess\\header";
    private static final String META_PROCESS_RESULT_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\main\\java\\testingmachine_backend\\json\\metalistwithprocess\\result";
    
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

    public static String getModuleId() {
        return moduleId != null ? moduleId : "";
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

    @Autowired
    public SystemService service;

    @GetMapping("/system-data")
    public List<SystemData> getAllSystemData() {
        return service.getAllSystemData(selectedModule);
    }

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
    }

    @PostMapping("/system-data")
    public ResponseEntity<SystemData> addSystemData(@RequestBody SystemData data) {
        clearStaticData();
        SystemData savedData = service.addSystemData(data);
        moduleId = savedData.getModuleId();
        customerName = savedData.getCustomerName();
        createdDate = ConfigProcess.DateUtils.getCurrentDateTime();
        systemURL = savedData.getSystemURL();
        username = savedData.getUsername();
        password = savedData.getPassword();
        databaseName = savedData.getDatabaseName();
        databaseUsername = savedData.getDatabaseUsername();
        jsonId = savedData.getGeneratedId();
        selectedModule =savedData.getSelectedModule();
        log.info("Created system data: ID = {},databaseName = {}, databaseUserame = {}, ModuleId = {}, CustomerName = {}, SystemURL = {}, username = {}, password = {}, selectedModule = {}",
                savedData.getGeneratedId(), savedData.getDatabaseName(), savedData.getDatabaseUsername(), savedData.getModuleId(), savedData.getCustomerName(),
                savedData.getSystemURL(), savedData.getUsername(), savedData.getPassword(), savedData.getSelectedModule());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
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


package testingmachine_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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

    @Getter
    public static String moduleId;
    @Getter
    public static String customerName;
    @Getter
    public static String  createdDate;
    @Getter
    public static String systemURL;
    @Getter
    public static String username;
    @Getter
    public static String password;
    @Getter
    public static String databaseName;
    @Getter
    public static String databaseUsername;
    @Getter
    public static String jsonId;
    @Getter
    public static String selectedModule;

    @Autowired
    public SystemService service;

    @GetMapping("/system-data")
    public List<SystemData> getAllSystemData() {
        return service.getAllSystemData(selectedModule);
    }

    private static void clearStaticData() {
        moduleId = null;
        customerName = null;
        createdDate = null;
        systemURL = null;
        username = null;
        password = null;
        databaseName = null;
        databaseUsername = null;
        jsonId = null;
        selectedModule = null;
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
        String processHeaderPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\header";
        return ResponseEntity.ok(readJsonFilesFromFolder(processHeaderPath));
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


    @GetMapping("/process-result")
    public ResponseEntity<List<FileData>> getResultData() {
        String processResultPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\result";
        return ResponseEntity.ok(readJsonFilesFromFolderResult(processResultPath));
    }

    private List<FileData> readJsonFilesFromFolderResult(String folderPath) {
        List<FileData> fileDataList = new ArrayList<>();
        File folder = new File(folderPath);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    Object data = objectMapper.readValue(jsonFile, Object.class);
                    String fileName = jsonFile.getName();
                    String customerName = getCustomerName();
                    String testUrl = getSystemURL();
                    if (fileName.endsWith("_result.json")) {
                        fileName = fileName.substring(0, fileName.length() - "_result.json".length());
                    }
                    fileDataList.add(new FileData(fileName, data, customerName, testUrl));
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return fileDataList;
    }


    @GetMapping("/meta-header")
    public ResponseEntity<List<Object>> getMetaHeaderData() {
        String metaHeaderPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\metalist\\header";
        return ResponseEntity.ok(readJsonFilesFromFolder(metaHeaderPath));
    }
    @GetMapping("/meta-result")
    public ResponseEntity<List<FileData>> getMetaResultData() {
        String metaResultPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\metalist\\result";
        return ResponseEntity.ok(readJsonFilesFromFolderResult(metaResultPath));
    }

    @GetMapping("/metaprocess-header")
    public ResponseEntity<List<Object>> getMetaProcessHeaderData() {
        String metaHeaderPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\metalistwithprocess\\header";
        return ResponseEntity.ok(readJsonFilesFromFolder(metaHeaderPath));
    }
    @GetMapping("/metaprocess-result")
    public ResponseEntity<List<FileData>> getMetaProcessResultData() {
        String metaResultPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\metalistwithprocess\\result";
        return ResponseEntity.ok(readJsonFilesFromFolderResult(metaResultPath));
    }

}


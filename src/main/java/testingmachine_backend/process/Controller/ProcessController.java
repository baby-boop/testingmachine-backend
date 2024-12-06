package testingmachine_backend.process.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testingmachine_backend.process.Config.ConfigProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class ProcessController {

    @Getter
    private static String moduleId;
    @Getter
    private static String customerName;
    @Getter
    private static String  createdDate;
    @Getter
    private static String systemURL;
    @Getter
    private static String username;
    @Getter
    private static String password;
    @Getter
    private static String databaseName;
    @Getter
    private static String databaseUsername;
    @Getter
    private static String jsonId;
    @Getter
    private static String selectedModule;

    @Autowired
    private SystemService service;

    @GetMapping("/system-data")
    public List<SystemData> getAllSystemData() {
        return service.getAllSystemData(selectedModule);
    }

    @PostMapping("/system-data")
    public ResponseEntity<SystemData> addSystemData(@RequestBody SystemData data) {
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
        if (savedData != null) {
            log.info("Created system data: ID = {},databaseName = {}, databaseUserame = {}, ModuleId = {}, CustomerName = {}, SystemURL = {}, username = {}, password = {}, selectedModule = {}",
                    savedData.getGeneratedId(), savedData.getDatabaseUsername(), savedData.getDatabaseUsername(), savedData.getModuleId(), savedData.getCustomerName(),
                    savedData.getSystemURL(), savedData.getUsername(), savedData.getPassword(), savedData.getSelectedModule());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String headerPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\header";

    private final String resultPath = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\result";

    @GetMapping("/header")
    public ResponseEntity<List<Object>> getHeaderData() {
        return ResponseEntity.ok(readJsonFilesFromFolder(headerPath));
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
                    e.printStackTrace();
                }
            }
        }
        return jsonDataList;
    }


    @GetMapping("/result")
    public ResponseEntity<List<FileData>> getResultData() {
        return ResponseEntity.ok(readJsonFilesFromFolderResult(resultPath));
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
                    e.printStackTrace();
                }
            }
        }
        return fileDataList;
    }

}


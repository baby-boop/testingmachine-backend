package testingmachine_backend.process.Controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testingmachine_backend.process.DTO.*;
import testingmachine_backend.process.Messages.PopupMessage;
import testingmachine_backend.process.ProcessList;
import testingmachine_backend.process.Service.ProcessMessageStatusService;
import testingmachine_backend.process.utils.ElementsFunctionUtils;
import testingmachine_backend.process.Messages.IsProcessMessage;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProcessController {

    @GetMapping("/process-log")
    public List<ProcessLogDTO> getProcessLog() {
        return ElementsFunctionUtils.getProcessLogMessages();
    }

    @GetMapping("/empty-data")
    public List<EmptyDataDTO> getFailedProcess() {
        return ElementsFunctionUtils.getUniqueEmptyDataPath();
    }

    @GetMapping("/popup-message")
    public List<PopupMessageDTO> getPopupMessages() {
        return PopupMessage.getUniquePopupMessages();
    }

    @GetMapping("/popup-standart")
    public List<PopupStandardFieldsDTO> getPopupStandartMessages() {
        return ElementsFunctionUtils.getPopupStandartMessages();
    }

    @GetMapping("/process-status")
    public List<ProcessMessageStatusDTO> processMessageStatus(){
        return ProcessMessageStatusService.getProcessStatuses();
    }

    @GetMapping("/process-required")
    public List<RequiredTest> getRequiredMessages() {
        return ElementsFunctionUtils.getRequiredPathMessages();
    }


    @Autowired
    private SystemService service;

    @GetMapping("/system-data")
    public List<SystemData> getAllSystemData() {
        return service.getAllSystemData();
    }

    @PostMapping("/system-data")
    public SystemData addSystemData(@RequestBody SystemData data) {
        return service.addSystemData(data);
    }

    @DeleteMapping("/system-data/{id}")
    public void deleteSystemData(@PathVariable Long id) {
        service.deleteSystemData(id);
    }


    @GetMapping("/process-progress")
    public MessageProgressDTO getProcessProgress() {
        int warningCount = IsProcessMessage.getWarningCount();
        int errorCount = IsProcessMessage.getErrorCount();
        int infoCount = IsProcessMessage.getInfoCount();
        int successCount = IsProcessMessage.getSuccessCount();
        int failedCount = ProcessPath.getFailedCount();

        return new MessageProgressDTO(warningCount, errorCount, infoCount, successCount, failedCount);
    }

    @GetMapping("/process-count")
    public ProcessDTO getProcessCounts() {
        int totalProcessCount = ProcessList.getTotalCount();
        return new ProcessDTO(totalProcessCount);
    }

    @GetMapping("/process-save")
    public List<NotFoundSaveButtonDTO> processSaveButtonStatus(){
        return ProcessPath.getProcessSaveMessages();
    }

    @Getter
    private static String systemId;
    private static String systemName;
    @Getter
    private static String systemUrl;
    @Getter
    private static String username;
    @Getter
    private static String password;
    @Getter
    private static String database;

    @PostMapping("/system-information")
    public ResponseEntity<String> executeSystemId(@RequestBody Map<String, String> request) {
        systemId = request.get("systemId");
        systemName = request.get("systemName");
        systemUrl = request.get("systemUrl");
        username = request.get("systemUsername");
        password = request.get("systemPassword");
        database = request.get("systemDatabase");

        String responseMessage = "System ID received: " + systemId;
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/system-information")
    public ResponseEntity<Map<String, String>> getSystemInformation() {
        if ( systemName == null || systemUrl == null) {
            return ResponseEntity.status(404).body(Map.of("message", "System information not set."));
        }
        return ResponseEntity.ok(Map.of(
                "systemId", systemId,
                "systemName", systemName,
                "systemUrl", systemUrl
        ));
    }
}


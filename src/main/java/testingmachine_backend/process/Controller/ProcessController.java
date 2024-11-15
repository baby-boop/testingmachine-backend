package testingmachine_backend.process.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import testingmachine_backend.process.DTO.*;
import testingmachine_backend.process.Messages.PopupMessage;
import testingmachine_backend.process.ProcessList;
import testingmachine_backend.process.utils.ElementsFunctionUtils;
import testingmachine_backend.process.Messages.IsProcessMessage;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;
@RestController
public class ProcessController {

    @GetMapping("/process-log")
    public List<ProcessLogDTO> getProcessLog() {
        return ElementsFunctionUtils.getProcessLogMessages();
    }
    @GetMapping("/process-count")
    public ProcessDTO getProcessCounts() {
        int totalProcessCount = ProcessList.getTotalCount();
        return new ProcessDTO(totalProcessCount);
    }

    @GetMapping("process-progress")
    public MessageProgressDTO getProcessProgress() {
        int warningCount = IsProcessMessage.getWarningCount();
        int errorCount = IsProcessMessage.getErrorCount();
        int infoCount = IsProcessMessage.getInfoCount();
        int successCount = IsProcessMessage.getSuccessCount();
        int failedCount = ProcessPath.getFailedCount();
        return new MessageProgressDTO(warningCount, errorCount, infoCount, successCount, failedCount);
    }

    @GetMapping("/warning-process")
    public List<WarningMessageDTO> getWarningProcess() {
        return IsProcessMessage.getProcessWarningMessages();
    }
    @GetMapping("/error-process")
    public List<ErrorMessageDTO> getErrorProcess() {
        return IsProcessMessage.getProcessErrorMessages();
    }
    @GetMapping("/info-process")
    public List<InfoMessageDTO> getInfoProcess() {
        return IsProcessMessage.getProcessInfoMessages();
    }
    @GetMapping("/success-process")
    public List<SuccessMessageDTO> getSuccessProcess() {
        return IsProcessMessage.getProcessSuccessMessages();
    }
    @GetMapping("/empty-data")
    public List<EmptyDataDTO> getFailedProcess() {
        return ElementsFunctionUtils.getUniqueEmptyDataPath();
    }
    @GetMapping("/popup-message")
    public List<PopupMessageDTO> getPopupMessages() {
        return PopupMessage.getPopupMessages();
    }
}

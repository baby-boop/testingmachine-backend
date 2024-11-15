//package testingmachine_backend.process.Controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import testingmachine_backend.process.DTO.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class FakeController {
//
//    @GetMapping("/process-log")
//    public List<ProcessLogDTO> getProcessLog() {
//        List<ProcessLogDTO> processLogFields = new ArrayList<>();
//        processLogFields.add(new ProcessLogDTO("fileName1.txt", "1", "First log message"));
//        processLogFields.add(new ProcessLogDTO("fileName1.txt", "2", "Second log message"));
//        processLogFields.add(new ProcessLogDTO("fileName2.txt", "3", "Second log message"));
//        return processLogFields;
//    }
//
//    @GetMapping("/process-count")
//    public ProcessDTO getProcessCounts() {
//        int totalProcessCount = 150;
//        return new ProcessDTO(totalProcessCount);
//    }
//
//    @GetMapping("process-progress")
//    public MessageProgressDTO getProcessProgress() {
//        int warningCount = 43;
//        int errorCount = 14;
//        int infoCount = 6;
//        int successCount = 10;
//        int failedCount = 5;
//        return new MessageProgressDTO(warningCount, errorCount, infoCount, successCount, failedCount);
//    }
//
//    @GetMapping("/warning-process")
//    public List<WarningMessageDTO> getWarningProcess() {
//        List<WarningMessageDTO> warningMessages = new ArrayList<>();
//        warningMessages.add(new WarningMessageDTO("fileName1.txt", "4", "Warning message"));
//        warningMessages.add(new WarningMessageDTO("fileName1.txt", "5", "Warning message"));
//        warningMessages.add(new WarningMessageDTO("fileName2.txt", "6", "Warning message"));
//        return warningMessages;
//    }
//
//    @GetMapping("/error-process")
//    public List<ErrorMessageDTO> getErrorProcess() {
//        List<ErrorMessageDTO> errorMessages = new ArrayList<>();
//        errorMessages.add(new ErrorMessageDTO("fileName1.txt", "7", "Warning message"));
//        errorMessages.add(new ErrorMessageDTO("fileName1.txt", "8", "Warning message"));
//        return errorMessages;
//    }
//
//    @GetMapping("/info-process")
//    public List<InfoMessageDTO> getInfoProcess() {
//        List<InfoMessageDTO> infoMessages = new ArrayList<>();
//        infoMessages.add(new InfoMessageDTO("fileName1.txt", "9", "info message"));
//        infoMessages.add(new InfoMessageDTO("fileName2.txt", "10", "info message"));
//        return infoMessages;
//    }
//
//    @GetMapping("/success-process")
//    public List<SuccessMessageDTO> getSuccessProcess() {
//        List<SuccessMessageDTO> successMessages = new ArrayList<>();
//        successMessages.add(new SuccessMessageDTO("fileName1.txt", "11"));
//        successMessages.add(new SuccessMessageDTO("fileName2.txt", "12"));
//        return successMessages;
//    }
//
//    @GetMapping("/failed-process")
//    public List<FailedMessageDTO> getFailedProcess() {
//        List<FailedMessageDTO> failedMessages = new ArrayList<>();
//        failedMessages.add(new FailedMessageDTO("fileName1.txt", "13"));
//        failedMessages.add(new FailedMessageDTO("fileName2.txt", "14"));
//        return failedMessages;
//    }
//
//    @GetMapping("/empty-data")
//    public List<EmptyDataDTO> getEmptyProcess() {
//        List<EmptyDataDTO> emptyPath = new ArrayList<>();
//        emptyPath.add(new EmptyDataDTO("fileName1.txt", "1", "popup1", "Popup"));
//        emptyPath.add(new EmptyDataDTO("fileName2.txt", "2", "combo1", "Combo"));
//        emptyPath.add(new EmptyDataDTO("fileName2.txt", "2", "popup2", "Popup"));
//        return emptyPath;
//    }
//
//    @GetMapping("/popup-message")
//    public List<PopupMessageDTO> getPopupMessages() {
//        List<PopupMessageDTO> popupMessages = new ArrayList<>();
//        popupMessages.add(new PopupMessageDTO("fileName1.txt", "1", "popup2", "Error popup1"));
//        return popupMessages;
//    }
//}

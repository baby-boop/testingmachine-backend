package testingmachine_backend.process.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import testingmachine_backend.process.DTO.ProcessDTO;
import testingmachine_backend.process.DTO.ProcessErrorMessageDTO;
import testingmachine_backend.process.DTO.ProcessLogDTO;
import testingmachine_backend.process.ProcessList;
import testingmachine_backend.process.utils.trashMessage;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;
@RestController
public class ProcessController {

    @GetMapping("/process-log")
    public List<ProcessLogDTO> getProcessLog() {
        return ProcessPath.getProcessLogMessages();
    }
    @GetMapping("/process-count")
    public ProcessDTO getProcessCounts() {
        int processCount = ProcessList.getCheckCount();
        int totalProcessCount = ProcessList.getTotalCount();
        return new ProcessDTO(processCount, totalProcessCount);
    }
    @GetMapping("/process-error")
    public List<ProcessErrorMessageDTO> getProcessErrorMessage () {
        return trashMessage.getProcessErrorMessages();
    }
}

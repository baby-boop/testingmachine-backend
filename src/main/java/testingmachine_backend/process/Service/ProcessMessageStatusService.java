package testingmachine_backend.process.Service;

import org.springframework.stereotype.Service;
import testingmachine_backend.process.DTO.ProcessMessageStatusDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProcessMessageStatusService {

    private static final List<ProcessMessageStatusDTO> processMessageStatusList = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(ProcessMessageStatusService.class.getName());

    public static void addProcessStatus(String fileName, String id, String status, String messageText) {
        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, status, messageText);
        processMessageStatusList.add(statusDTO);
    }

    public static List<ProcessMessageStatusDTO> getProcessStatuses() {
        return new ArrayList<>(processMessageStatusList);
    }
}
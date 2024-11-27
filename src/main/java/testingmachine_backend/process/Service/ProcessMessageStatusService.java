package testingmachine_backend.process.Service;

import org.springframework.stereotype.Service;
import testingmachine_backend.process.DTO.ProcessMessageStatusDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessMessageStatusService {

    private static final List<ProcessMessageStatusDTO> processMessageStatusList = new ArrayList<>();

    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText) {
        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, code, name, status, messageText);
        processMessageStatusList.add(statusDTO);
    }

    public static List<ProcessMessageStatusDTO> getProcessStatuses() {
        return new ArrayList<>(processMessageStatusList);
    }
}
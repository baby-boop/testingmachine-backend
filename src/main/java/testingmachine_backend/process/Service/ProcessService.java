package testingmachine_backend.process.Service;

import org.springframework.stereotype.Service;
import testingmachine_backend.config.JsonMetaPersent;
import testingmachine_backend.process.DTO.ProcessDTO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ProcessService {

    private static ProcessService instance;
    private final List<ProcessDTO> processResults = new CopyOnWriteArrayList<>();

    private ProcessService() {}

    public static ProcessService getInstance() {
        if (instance == null) {
            synchronized (ProcessService.class) {
                if (instance == null) {
                    instance = new ProcessService();
                }
            }
        }
        return instance;
    }

    public void updateOrAddProcessResult(ProcessDTO processDTO) {
        ProcessDTO existingDTO = processResults.stream()
                .filter(dto -> dto.getId().equals(processDTO.getId()) &&
                        dto.getTotalProcessCount() == processDTO.getTotalProcessCount() &&
                        dto.getCustomerName().equals(processDTO.getCustomerName()) &&
                        dto.getCreatedDate().equals(processDTO.getCreatedDate()) &&
                        dto.getJsonId().equals(processDTO.getJsonId()) &&
                        dto.getModuleId().equals(processDTO.getModuleId()))
                .findFirst()
                .orElse(null);

        if (existingDTO != null) {
            existingDTO.setProcessCount(processDTO.getProcessCount());
        } else {
            processResults.add(processDTO);
        }
        JsonMetaPersent.saveToSingleJsonFile(processDTO, existingDTO.getJsonId());
    }

    public List<ProcessDTO> getProcessResults() {
        return processResults;
    }
}

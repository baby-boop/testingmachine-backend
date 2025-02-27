package testingmachine_backend.projects.process.Service;

import org.springframework.stereotype.Service;
import testingmachine_backend.projects.process.DTO.ProcessDTO;

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
                        dto.getModuleId().equals(processDTO.getModuleId()) &&
                        dto.getSystemUrl().equals(processDTO.getSystemUrl()))
                .findFirst()
                .orElse(null);

        if (existingDTO != null) {
            existingDTO.setProcessCount(processDTO.getProcessCount());
        } else {
            processResults.add(processDTO);
        }
//        JsonPersentHeader.saveToSingleJsonFile(processDTO, processDTO.getJsonId()); //jsonPercent

    }

    public List<ProcessDTO> getProcessResults() {
        return processResults;
    }
}

package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProcessStatusNestedDTO {
    private String id;
    private String name;
    private List<ProcessMessageStatusDTO> processStatus; // Changed to a list of ProcessMessageStatusDTO

    public ProcessStatusNestedDTO(String id, String name, List<ProcessMessageStatusDTO> processStatus) {
        this.id = id;
        this.name = name;
        this.processStatus = processStatus;
    }
}

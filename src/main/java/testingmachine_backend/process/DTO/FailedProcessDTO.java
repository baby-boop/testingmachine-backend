package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailedProcessDTO {

    private String fileName;
    private String processId;

    public FailedProcessDTO(String fileName, String processId) {
        this.fileName = fileName;
        this.processId = processId;
    }
}

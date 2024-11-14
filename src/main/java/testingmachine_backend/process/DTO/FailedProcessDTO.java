package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailedProcessDTO {

    private String fileName;
    private String processId;
    private String dataPath;

    public FailedProcessDTO(String fileName, String processId, String dataPath) {
        this.fileName = fileName;
        this.processId = processId;
        this.dataPath = dataPath;
    }
}

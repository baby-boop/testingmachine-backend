package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailedMessageDTO {
    private String fileName;
    private String processId;

    public FailedMessageDTO(String fileName, String processId) {
        this.fileName = fileName;
        this.processId = processId;

    }
}

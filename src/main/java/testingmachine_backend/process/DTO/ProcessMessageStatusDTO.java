package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessMessageStatusDTO {

    private String fileName;
    private String processId;
    private String status;
    private String messageText;

    public ProcessMessageStatusDTO(String fileName, String processId, String status, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.status = status;
        this.messageText = messageText;
    }
}

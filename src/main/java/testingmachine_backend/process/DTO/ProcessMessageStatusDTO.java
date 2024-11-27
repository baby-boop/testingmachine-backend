package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessMessageStatusDTO {

    private String fileName;
    private String processId;
    private String processCode;
    private String processName;
    private String status;
    private String messageText;

    public ProcessMessageStatusDTO(String fileName, String processId, String processCode, String processName, String status, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.processCode = processCode;
        this.processName = processName;
        this.status = status;
        this.messageText = messageText;
    }
}

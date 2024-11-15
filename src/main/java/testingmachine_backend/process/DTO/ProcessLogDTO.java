package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

public class ProcessLogDTO {


    @Setter
    @Getter
    private String fileName;
    @Setter
    @Getter
    private String processId;
    private String messageText;

    public ProcessLogDTO(String fileName, String processId, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.messageText = messageText;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessage(String messageText) {
        this.messageText = messageText;
    }
}

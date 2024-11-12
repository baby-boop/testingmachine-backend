package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

public class WarningMessageDTO {
    @Getter
    @Setter
    private String fileName;
    @Getter
    @Setter
    private String processId;
    private String messageText;

    public WarningMessageDTO(String fileName, String processId, String messageText) {
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

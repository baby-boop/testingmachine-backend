package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupMessageDTO {
    private String fileName;

    private String processId;
    private String messageText;

    public PopupMessageDTO(String fileName, String processId, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.messageText = messageText;
    }
}

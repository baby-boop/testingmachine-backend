package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequiredPathDTO {

    private String fileName;
    private String processId;
    private String logType;
    private String messageText;
    private String jsonId;


    public RequiredPathDTO(String fileName, String processId, String logType, String messageText, String jsonId) {
        this.fileName = fileName;
        this.processId = processId;
        this.logType = logType;
        this.messageText = messageText;
        this.jsonId = jsonId;
    }


}

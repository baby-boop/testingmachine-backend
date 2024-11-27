package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequiredTest {

    private String fileName;
    private String processId;
    private String logType;
    private String messageText;


    public RequiredTest(String fileName, String processId, String logType, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.logType = logType;
        this.messageText = messageText;
    }


}

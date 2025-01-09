package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequiredPathDTO {

    private String moduleName;
    private String metaDataId;
    private String logType;
    private String messageText;
    private String jsonId;


    public RequiredPathDTO(String moduleName, String metaDataId, String logType, String messageText, String jsonId) {
        this.moduleName = moduleName;
        this.metaDataId = metaDataId;
        this.logType = logType;
        this.messageText = messageText;
        this.jsonId = jsonId;
    }


}

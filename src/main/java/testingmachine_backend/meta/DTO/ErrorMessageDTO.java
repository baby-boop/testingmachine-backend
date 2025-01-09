package testingmachine_backend.meta.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessageDTO {
    private String moduleName;
    private String metaDataId;
    private String metaDataCode;
    private String metaDataName;
    private String status;
    private String messageText;
    private String jsonId;

    public ErrorMessageDTO(String moduleName, String metaDataId, String metaDataCode, String metaDataName, String status, String messageText, String jsonId) {
        this.moduleName = moduleName;
        this.metaDataId = metaDataId;
        this.metaDataCode = metaDataCode;
        this.metaDataName = metaDataName;
        this.status = status;
        this.messageText = messageText;
        this.jsonId = jsonId;
    }

}

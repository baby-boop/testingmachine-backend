package testingmachine_backend.meta.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessageDTO {
    private String moduleName;
    private String metaId;
    private String metaCode;
    private String metaName;
    private String metaType;
    private String messageText;
    private String jsonId;

    public ErrorMessageDTO(String moduleName, String metaId, String metaCode, String metaName, String metaType, String messageText, String jsonId) {
        this.moduleName = moduleName;
        this.metaId = metaId;
        this.metaCode = metaCode;
        this.metaName = metaName;
        this.metaType = metaType;
        this.messageText = messageText;
        this.jsonId = jsonId;
    }

}

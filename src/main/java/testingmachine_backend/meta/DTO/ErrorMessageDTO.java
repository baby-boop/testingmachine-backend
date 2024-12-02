package testingmachine_backend.meta.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessageDTO {
    private String fileName;
    private String metaId;
    private String metaCode;
    private String metaName;
    private String metaType;
    private String messageText;

    public ErrorMessageDTO(String fileName, String metaId, String metaCode, String metaName, String metaType, String messageText) {
        this.fileName = fileName;
        this.metaId = metaId;
        this.metaCode = metaCode;
        this.metaName = metaName;
        this.metaType = metaType;
        this.messageText = messageText;
    }


}

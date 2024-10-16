package testingmachine_backend.metaverse.DTO;

public class MVerrorMessageDTO {

    private String fileName;
    private String metaId;
    private String metaCode;
    private String messageText;

    public MVerrorMessageDTO(String fileName, String metaId, String metaCode, String messageText) {
        this.fileName = fileName;
        this.metaId = metaId;
        this.metaCode = metaCode;
        this.messageText = messageText;
    }

    // Getters and setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMetaId() {
        return metaId;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }

    public String getMetaCode() {
        return metaCode;
    }

    public void setMetaCode(String metaCode) {
        this.metaCode = metaCode;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessage(String messageText) {
        this.messageText = messageText;
    }
}

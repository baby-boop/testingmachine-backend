package testingmachine_backend.process.DTO;

public class ProcessLogDTO {


    private String fileName;
    private String processId;
    private String messageText;

    public ProcessLogDTO(String fileName, String processId, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.messageText = messageText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessage(String messageText) {
        this.messageText = messageText;
    }
}

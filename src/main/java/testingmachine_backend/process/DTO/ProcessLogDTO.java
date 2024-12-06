package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class ProcessLogDTO {

    private String fileName;
    private String processId;
    private String logType;
    private String messageText;
    private String jsonId;

    public ProcessLogDTO(String fileName, String processId, String logType, String messageText, String jsonId) {
        this.fileName = fileName;
        this.processId = processId;
        this.logType = logType;
        this.messageText = messageText;
        this.jsonId = jsonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessLogDTO that = (ProcessLogDTO) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(logType, that.logType) &&
                Objects.equals(messageText, that.messageText) &&
                Objects.equals(jsonId, that.jsonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, processId, logType, messageText, jsonId);
    }
}




package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class ProcessLogDTO {

    private String moduleName;
    private String processId;
    private String logType;
    private String messageText;
    private String jsonId;

    public ProcessLogDTO(String moduleName, String processId, String logType, String messageText, String jsonId) {
        this.moduleName = moduleName;
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
        return Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(logType, that.logType) &&
                Objects.equals(messageText, that.messageText) &&
                Objects.equals(jsonId, that.jsonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, processId, logType, messageText, jsonId);
    }
}




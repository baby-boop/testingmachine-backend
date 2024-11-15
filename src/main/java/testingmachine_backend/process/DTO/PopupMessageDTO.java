package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PopupMessageDTO {
    private String fileName;

    private String processId;
    private String dataPath;
    private String messageText;

    public PopupMessageDTO(String fileName, String processId, String dataPath, String messageText) {
        this.fileName = fileName;
        this.processId = processId;
        this.dataPath = dataPath;
        this.messageText = messageText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopupMessageDTO that = (PopupMessageDTO) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(dataPath, that.dataPath) &&
                Objects.equals(messageText, that.messageText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, processId, dataPath, messageText);
    }
}

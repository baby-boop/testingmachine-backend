package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PopupMessageDTO {
    private String moduleName;

    private String metaDataId;
    private String dataPath;
    private String messageText;
    private String jsonId;

    public PopupMessageDTO(String moduleName, String metaDataId, String dataPath, String messageText, String jsonId) {
        this.moduleName = moduleName;
        this.metaDataId = metaDataId;
        this.dataPath = dataPath;
        this.messageText = messageText;
        this.jsonId = jsonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopupMessageDTO that = (PopupMessageDTO) o;
        return Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(metaDataId, that.metaDataId) &&
                Objects.equals(dataPath, that.dataPath) &&
                Objects.equals(messageText, that.messageText) &&
                Objects.equals(jsonId, that.jsonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, metaDataId, dataPath, messageText, jsonId);
    }
}

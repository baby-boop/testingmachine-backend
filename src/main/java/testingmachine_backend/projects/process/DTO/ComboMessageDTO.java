package testingmachine_backend.projects.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ComboMessageDTO {

    private String moduleName;
    private String metaDataId;
    private String dataPath;
    private String jsonId;
    private String message;

    public ComboMessageDTO(String moduleName, String metaDataId, String dataPath,  String jsonId, String message) {
        this.moduleName = moduleName;
        this.metaDataId = metaDataId;
        this.dataPath = dataPath;
        this.jsonId = jsonId;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComboMessageDTO that = (ComboMessageDTO) o;
        return Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(metaDataId, that.metaDataId) &&
                Objects.equals(dataPath, that.dataPath) &&
                Objects.equals(jsonId, that.jsonId) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, metaDataId, dataPath, jsonId, message);
    }
}

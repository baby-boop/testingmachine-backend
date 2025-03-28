package testingmachine_backend.projects.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class EmptyDataDTO {

    private String moduleName;
    private String metaDataId;
    private String dataPath;
    private String dataType;
    private String jsonId;

    public EmptyDataDTO(String moduleName, String metaDataId, String dataPath, String dataType, String jsonId) {
        this.moduleName = moduleName;
        this.metaDataId = metaDataId;
        this.dataPath = dataPath;
        this.dataType = dataType;
        this.jsonId = jsonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmptyDataDTO that = (EmptyDataDTO) o;
        return Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(metaDataId, that.metaDataId) &&
                Objects.equals(dataPath, that.dataPath) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(jsonId, that.jsonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, metaDataId, dataPath, dataType, jsonId);
    }
}

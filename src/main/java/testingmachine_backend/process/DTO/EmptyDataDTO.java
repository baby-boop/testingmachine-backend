package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class EmptyDataDTO {

    private String fileName;
    private String processId;
    private String dataPath;
    private String dataType;
    private String jsonId;

    public EmptyDataDTO(String fileName, String processId, String dataPath, String dataType, String jsonId) {
        this.fileName = fileName;
        this.processId = processId;
        this.dataPath = dataPath;
        this.dataType = dataType;
        this.jsonId = jsonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmptyDataDTO that = (EmptyDataDTO) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(dataPath, that.dataPath) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(jsonId, that.jsonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, processId, dataPath, dataType, jsonId);
    }
}

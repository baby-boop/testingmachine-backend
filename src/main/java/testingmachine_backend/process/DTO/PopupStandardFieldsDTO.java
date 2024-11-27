package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class PopupStandardFieldsDTO {

    private String fileName;
    private String processId;
    private String dataPath;
    private String dataType;

    public PopupStandardFieldsDTO(String fileName, String processId, String dataPath, String dataType) {
        this.fileName = fileName;
        this.processId = processId;
        this.dataPath = dataPath;
        this.dataType = dataType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopupStandardFieldsDTO that = (PopupStandardFieldsDTO) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(dataPath, that.dataPath) &&
                Objects.equals(dataType, that.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, processId, dataPath, dataType);
    }

}

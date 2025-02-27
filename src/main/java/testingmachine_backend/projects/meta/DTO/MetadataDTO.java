package testingmachine_backend.projects.meta.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetadataDTO {

    private String id;
    private String moduleName;
    private String code;
    private String name;
    private String processName;

    public MetadataDTO(String id, String moduleName, String code, String name, String processName) {
        this.id = id;
        this.moduleName = moduleName;
        this.code = code;
        this.name = name;
        this.processName = processName;
    }
}

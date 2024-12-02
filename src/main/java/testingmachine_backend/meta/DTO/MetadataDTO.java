package testingmachine_backend.meta.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetadataDTO {

    private String id;
    private String moduleName;
    private String code;
    private String name;


    public MetadataDTO(String id, String moduleName, String code, String name) {
        this.id = id;
        this.moduleName = moduleName;
        this.code = code;
        this.name = name;
    }

}

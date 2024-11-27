package testingmachine_backend.meta.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessMetaData {
    private String id;
    private String systemName;
    private String code;
    private String name;

    public ProcessMetaData(String id, String systemName, String code, String name) {
        this.id = id;
        this.systemName = systemName;
        this.code = code;
        this.name = name;
    }

}

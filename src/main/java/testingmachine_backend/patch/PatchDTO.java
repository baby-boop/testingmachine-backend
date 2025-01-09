package testingmachine_backend.patch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchDTO {

    private String id;
    private String code;
    private String name;
    private String typeId;
    private String patchId;
    private String patchName;

    public PatchDTO(String id, String code, String name, String typeId, String patchId, String patchName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.typeId = typeId;
        this.patchId = patchId;
        this.patchName = patchName;
    }
}

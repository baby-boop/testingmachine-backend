package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotFoundSaveButtonDTO {

    private String fileName;
    private String processId;

    public NotFoundSaveButtonDTO(String fileName, String processId) {
        this.fileName = fileName;
        this.processId = processId;

    }
}

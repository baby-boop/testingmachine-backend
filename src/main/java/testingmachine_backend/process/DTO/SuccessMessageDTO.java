package testingmachine_backend.process.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessMessageDTO {

    private String fileName;
    private String processId;


    public SuccessMessageDTO(String fileName, String processId) {
        this.fileName = fileName;
        this.processId = processId;

    }
}

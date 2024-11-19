package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessDTO {

    private final int totalProcessCount;
    public ProcessDTO(int totalProcessCount) {
        this.totalProcessCount = totalProcessCount;
    }

}

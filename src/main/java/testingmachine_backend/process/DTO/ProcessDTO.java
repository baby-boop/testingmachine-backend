package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
public class ProcessDTO {

    public String id;
    public int totalProcessCount;
    public int processCount;
    public String customerName;
    public String createdDate;
    public  String jsonId;
    public String moduleId;

    public ProcessDTO(String id,int totalProcessCount, int processCount, String customerName, String createdDate, String jsonId, String moduleId) {
        this.id = id;
        this.totalProcessCount = totalProcessCount;
        this.processCount = processCount;
        this.customerName = customerName;
        this.createdDate = createdDate;
        this.jsonId = jsonId;
        this.moduleId = moduleId;
    }
}

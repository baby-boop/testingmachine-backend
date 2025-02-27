package testingmachine_backend.projects.process.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageProgressDTO {

    private int warningCount;
    private int errorCount;
    private int infoCount;
    private int successCount;
    private int failedCount;

    public MessageProgressDTO(int warningCount, int errorCount, int infoCount, int successCount, int failedCount) {
        this.warningCount = warningCount;
        this.errorCount = errorCount;
        this.infoCount = infoCount;
        this.successCount = successCount;
        this.failedCount = failedCount;
    }
}

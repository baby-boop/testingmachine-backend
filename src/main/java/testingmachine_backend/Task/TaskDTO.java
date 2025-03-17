package testingmachine_backend.Task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
public class TaskDTO {
    private String taskName;
    private String interval;
    private LocalDate lastUpdatedDate = LocalDate.now();
    private String moduleId;
    private boolean due;

    public boolean isDue() {

        if (lastUpdatedDate == null) {
            lastUpdatedDate = LocalDate.now().minusDays(1);
        }

        long amount = Long.parseLong(interval.replaceAll("\\D+", ""));
        String unit = interval.replaceAll("\\d+", "").trim().toLowerCase();

        LocalDate nextExecutionDate;
        switch (unit) {
            case "day":
            case "days":
                nextExecutionDate = lastUpdatedDate.plus(amount, ChronoUnit.DAYS);
                break;
            case "hour":
            case "hours":
                nextExecutionDate = lastUpdatedDate.plus(amount / 24, ChronoUnit.DAYS);
                break;
            case "minute":
            case "minutes":
                nextExecutionDate = lastUpdatedDate.plus(amount / 1440, ChronoUnit.DAYS);
                break;
            default:
                System.err.println("Unsupported interval format for task: " + taskName);
                return false;
        }
        return LocalDate.now().isAfter(nextExecutionDate) || LocalDate.now().isEqual(nextExecutionDate);
    }
}

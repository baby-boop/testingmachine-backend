package testingmachine_backend.schedule;

import java.time.LocalDateTime;

public class ScheduledData {

    private String dataId;
    private LocalDateTime scheduledTime;

    public ScheduledData(String dataId, LocalDateTime scheduledTime) {
        this.dataId = dataId;
        this.scheduledTime = scheduledTime;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}

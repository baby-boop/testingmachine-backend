package testingmachine_backend.schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDTO {
    private String username;
    private String password;
    private String unitName;
    private String moduleId;
    private String scheduleTime;
}

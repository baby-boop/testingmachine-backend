package testingmachine_backend.process.Controller;

import lombok.Getter;
import lombok.Setter;
import testingmachine_backend.process.Config.ConfigProcess;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class SystemData {

    private Long id;
    private String moduleId;
    private String customerName;
    private String createdDate = ConfigProcess.DateUtils.getCurrentDateTime();
    private String systemURL;
    private String username;
    private String password;
    private String databaseName;
    private String databaseUsername;
    private String generatedId;
    private String selectedModule;

}

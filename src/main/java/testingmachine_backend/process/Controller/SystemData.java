package testingmachine_backend.process.Controller;

import lombok.Getter;
import lombok.Setter;
import testingmachine_backend.process.Config.ConfigProcess;
import java.util.Random;

@Getter
@Setter
public class SystemData {

    private String id;
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
    private String metaOrPatchId;
    private String isCheckBox;

    public SystemData() {
        this.id = generateUniqueId();
    }
    private String generateUniqueId() {
        Random random = new Random();
        int randomNumber = 1000000 + random.nextInt(9000000);
        return "14" + randomNumber;
    }
}

package testingmachine_backend.projects.process;

import org.openqa.selenium.WebDriver;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.config.WebDriverManager.*;

public class MainProcess {

    private static final Logger logger = Logger.getLogger(MainProcess.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) {

        WebDriver driver = getDriverManager();

        try {

            ProcessList tool = new ProcessList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, processId, isLoginCheckBox);

        } catch (Exception e) {
            quitDriverManager();
            logger.log(Level.SEVERE, "Error mainProcess, {0}", e);
        } finally {
            quitDriverManager();
        }
    }

}

package testingmachine_backend.projects.patch;

import org.openqa.selenium.WebDriver;
import testingmachine_backend.projects.process.MainProcess;

import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.config.WebDriverManager.*;

public class patchMain {

    private static final Logger logger = Logger.getLogger(MainProcess.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String patchId, String isLoginCheckBox) {

        WebDriver driver = getDriverManager();

        try {

            patchList tool = new patchList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, patchId, isLoginCheckBox);

        } catch (Exception e) {
            quitDriverManager();
            logger.log(Level.SEVERE, "Error patchMain, {0}", e);
        } finally {
            quitDriverManager();
        }
    }
}

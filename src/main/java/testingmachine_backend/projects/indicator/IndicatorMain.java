package testingmachine_backend.projects.indicator;

import org.openqa.selenium.WebDriver;
import testingmachine_backend.projects.process.MainProcess;

import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.config.WebDriverManager.*;

public class IndicatorMain {

    private static final Logger logger = Logger.getLogger(MainProcess.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) {

        WebDriver driver =  getDriverManager();

        try {

            IndicatorList tool = new IndicatorList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, processId, isLoginCheckBox);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error mainIndicator: ", e);
            quitDriverManager();
        } finally {
            quitDriverManager();
        }
    }

}

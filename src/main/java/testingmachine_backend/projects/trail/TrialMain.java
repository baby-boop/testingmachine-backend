package testingmachine_backend.projects.trail;

import org.openqa.selenium.WebDriver;
import testingmachine_backend.projects.process.MainProcess;
import testingmachine_backend.projects.product.CallModuleIndicatorList;
import testingmachine_backend.projects.product.ProductList;

import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.config.WebDriverManager.getDriverManager;
import static testingmachine_backend.config.WebDriverManager.quitDriverManager;

public class TrialMain {

    private static final Logger logger = Logger.getLogger(TrialMain.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) {

        WebDriver driver = getDriverManager();

        try {

            TrailList tool = new TrailList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, processId, isLoginCheckBox);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error TrialMain: ", e);
            quitDriverManager();
        } finally {
            quitDriverManager();
        }
    }
}

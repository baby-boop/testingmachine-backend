package testingmachine_backend.projects.product;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import testingmachine_backend.projects.process.MainProcess;

import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.config.WebDriverManager.*;

public class ProductMain {

    private static final Logger logger = Logger.getLogger(ProductMain.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) {
        WebDriver driver = getDriverManager();
        try {
            ProductList tool = new ProductList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, processId, isLoginCheckBox);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error mainIndicator: ", e);
            quitDriverManager();
        } finally {
            quitDriverManager();
        }
    }
}

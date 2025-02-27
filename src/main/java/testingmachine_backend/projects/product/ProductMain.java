package testingmachine_backend.projects.product;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import testingmachine_backend.projects.process.MainProcess;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductMain {

    private static final Logger logger = Logger.getLogger(MainProcess.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) {
        Map<String, String> loggingPrefs = Map.of(
                LogType.BROWSER, "ALL"
        );
        ChromeOptions options = getChromeOptions();

        options.setCapability("goog:loggingPrefs", loggingPrefs);
        WebDriver driver = new ChromeDriver(options);

        try {

            ProductList tool = new ProductList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, processId, isLoginCheckBox);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error mainIndicator: ", e);
        } finally {
//            driver.quit();
        }
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
//                "--headless",
                "--disable-gpu",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );
        return options;
    }
}

package testingmachine_backend.indicator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import testingmachine_backend.process.MainProcess;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndicatorMain {

    private static final Logger logger = Logger.getLogger(MainProcess.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String processId) {
        Map<String, String> loggingPrefs = Map.of(
                LogType.BROWSER, "ALL"
        );
        ChromeOptions options = getChromeOptions();

        options.setCapability("goog:loggingPrefs", loggingPrefs);
        WebDriver driver = new ChromeDriver(options);

        try {

            IndicatorList tool = new IndicatorList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, processId);

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

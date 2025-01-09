package testingmachine_backend.patch;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import testingmachine_backend.process.MainProcess;
import testingmachine_backend.process.ProcessList;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class patchMain {

    private static final Logger logger = Logger.getLogger(MainProcess.class.getName());

    public static void mainProcess(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String patchId) {
        Map<String, String> loggingPrefs = Map.of(
                LogType.BROWSER, "ALL"
        );

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
//                "--headless",
                "--no-sandbox",
                "--disable-gpu",
                "--ignore-ssl-errors=yes",
                "--ignore-certificate-errors",
                "--disable-dev-shm-usage"
        );
        options.setCapability("goog:loggingPrefs", loggingPrefs);
        WebDriver driver = new ChromeDriver(options);

        try {

            patchList tool = new patchList(driver);
            tool.mainTool(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, patchId);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error RemoteWebDriver", e);
        }
    }
}

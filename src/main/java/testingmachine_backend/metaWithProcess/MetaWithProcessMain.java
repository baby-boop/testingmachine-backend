package testingmachine_backend.metaWithProcess;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;

import java.util.Map;

public class MetaWithProcessMain {


    public static void mainProcess(String jsonId) {

        Map<String, String> loggingPrefs = Map.of(
                LogType.BROWSER, "ALL"
        );

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.setCapability("goog:loggingPrefs", loggingPrefs);

        WebDriver driver = new ChromeDriver(options);

        try{
            MetaWithProcessList tool = new MetaWithProcessList(driver);

            tool.mainTool(jsonId);
        }finally {
//            driver.quit();
            System.out.println("completed");
        }
    }
}

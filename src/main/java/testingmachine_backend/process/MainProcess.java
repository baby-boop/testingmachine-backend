package testingmachine_backend.process;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;

import java.util.Map;

public class MainProcess {


    public static void mainProcess() {

        Map<String, String> loggingPrefs = Map.of(
                LogType.BROWSER, "ALL"
        );

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        options.setCapability("goog:loggingPrefs", loggingPrefs);

        WebDriver driver = new ChromeDriver(options);

        try{
            ProcessList tool = new ProcessList(driver);

            tool.mainTool();
        }catch (Exception e){
//            driver.quit();
        }
    }
}

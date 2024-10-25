package testingmachine_backend.process;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import testingmachine_backend.meta.Utils.CheckWorkflow;

import java.util.logging.Logger;

public class MainProcess {

    private static final Logger LOGGER = Logger.getLogger(CheckWorkflow.class.getName());

    public static void mainProcess() {

        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        try{

            ProcessList tool = new ProcessList(driver);

            tool.mainTool();

        }catch (Exception e){

            driver.quit();
            LOGGER.info(e.getMessage());
        }finally {
//            driver.quit();
        }
    }
}

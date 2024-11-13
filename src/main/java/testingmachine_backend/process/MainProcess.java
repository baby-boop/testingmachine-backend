package testingmachine_backend.process;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MainProcess {


    public static void mainProcess() {

        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        try{
            ProcessList tool = new ProcessList(driver);

            tool.mainTool();
        }catch (Exception e){
//            driver.quit();

        }finally {

//            driver.quit();
        }
    }
}

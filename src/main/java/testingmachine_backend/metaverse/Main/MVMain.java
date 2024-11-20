package testingmachine_backend.metaverse.Main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MVMain {

    public  static void mainSystem(){
        ChromeOptions options = new ChromeOptions();
        /*         options.addArguments("--headless");*/
        WebDriver driver = new ChromeDriver(options);
        try{

            MVLists main = new MVLists(driver);

            main.mainList();

        }finally {
//            driver.quit();
            System.out.println("completed");

        }
    }
}

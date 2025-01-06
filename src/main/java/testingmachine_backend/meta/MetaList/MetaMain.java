package testingmachine_backend.meta.MetaList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import testingmachine_backend.controller.JsonController;

public class MetaMain {

    public  static void mainSystem(String jsonId){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        WebDriver driver = new ChromeDriver(options);
        try{

            MetaLists main = new MetaLists(driver);

            main.mainList(jsonId);

        }finally {
            driver.quit();
            System.out.println("completed");

        }
    }
}
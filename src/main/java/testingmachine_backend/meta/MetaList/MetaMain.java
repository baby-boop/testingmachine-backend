package testingmachine_backend.meta.MetaList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MetaMain {

    public  static void mainSystem(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password){


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        WebDriver driver = new ChromeDriver(options);
        try{

            MetaLists main = new MetaLists(driver);

            main.mainList(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password);

        }finally {
            driver.quit();
            System.out.println("completed");

        }
    }
}
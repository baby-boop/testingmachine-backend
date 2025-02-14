package testingmachine_backend.meta.MetaList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import testingmachine_backend.config.WebDriverManager;

public class MetaMain {

    public  static void mainSystem(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password){


        WebDriver driver = WebDriverManager.getDriver();

        try {

            MetaLists main = new MetaLists(driver);

            main.mainList(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password);

        }finally {
            WebDriverManager.quitDriver();
            System.out.println("completed");

        }
    }
}
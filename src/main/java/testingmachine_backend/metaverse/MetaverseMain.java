package testingmachine_backend.metaverse;

import org.openqa.selenium.WebDriver;
import testingmachine_backend.config.WebDriverManager;

public class MetaverseMain {

    public static void mainSystem(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String isLoginCheckBox){


        WebDriver driver = WebDriverManager.getDriver();

        try {

            MetaverseList main = new MetaverseList(driver);

            main.mainList(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, isLoginCheckBox);

        }finally {
            WebDriverManager.quitDriver();
            System.out.println("completed");

        }
    }
}

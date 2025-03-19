package testingmachine_backend.projects.metaverse;

import org.openqa.selenium.WebDriver;
import static testingmachine_backend.config.WebDriverManager.*;

public class MetaverseMain {

    public static void mainSystem(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String databaseName, String unitName, String systemUrl, String username, String password, String isLoginCheckBox){


        WebDriver driver = getDriverManager();

        try {

            MetaverseList main = new MetaverseList(driver);

            main.mainList(jsonId, theadId, customerName, createdDate, moduleId, databaseName, unitName, systemUrl, username, password, isLoginCheckBox);

        }catch (Exception e){
            quitDriverManager();
        }
        finally {
            quitDriverManager();
            System.out.println("completed");

        }
    }
}

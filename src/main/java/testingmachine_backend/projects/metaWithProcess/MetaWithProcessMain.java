package testingmachine_backend.projects.metaWithProcess;

import org.openqa.selenium.WebDriver;

import static testingmachine_backend.config.WebDriverManager.*;

public class MetaWithProcessMain {


    public static void mainProcess(String jsonId) {

        WebDriver driver = getDriverManager();

        try{
            MetaWithProcessList tool = new MetaWithProcessList(driver);

            tool.mainTool(jsonId);
        }catch(Exception e){
            quitDriverManager();
        }
        finally {
            quitDriverManager();
        }
    }
}

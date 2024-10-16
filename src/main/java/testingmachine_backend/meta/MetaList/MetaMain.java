package testingmachine_backend.meta.MetaList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MetaMain {

    public void mainSystem(){
        ChromeOptions options = new ChromeOptions();
/*         options.addArguments("--headless");*/
        WebDriver driver = new ChromeDriver(options);
        try{

            MetaLists main = new MetaLists(driver);
            MetaWithWorkflow workflow = new MetaWithWorkflow(driver);
            MetaWithExcel excel = new MetaWithExcel(driver);

            main.mainList();
//            workflow.mainList();
//            excel.mainList();

        }finally {
            driver.quit();
            System.out.println("completed");

        }
    }
}
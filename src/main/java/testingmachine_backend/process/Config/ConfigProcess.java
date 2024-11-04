package testingmachine_backend.process.Config;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.utils.WaitElement;

import java.time.Duration;

public class ConfigProcess {

    public static final String BaseUrl = "https://dev.veritech.mn";
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String USERNAME = "batdelger";
    public static final String PASSWORD = "123";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/";

    public static final int TIMEOUT = 10;

    public static WebDriverWait getWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public static void waitUtils (WebDriver driver) {
        try{

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            WaitElement.retryWaitForLoadToDisappear(driver, 3);
            WaitElement.retryWaitForLoadingToDisappear(driver, 3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

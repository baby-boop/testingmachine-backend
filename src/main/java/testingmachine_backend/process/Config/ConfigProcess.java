package testingmachine_backend.process.Config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.process.utils.WaitElement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ConfigProcess {


    public static final String BaseUrl = "https://"+ JsonController.getSystemURL();
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String MainUrl = BaseUrl + "/mdprocess/renderByTestTool/";

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

    public static class DateUtils {

        public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public static String getCurrentDate() {
            LocalDateTime sysDate = LocalDateTime.now();
            return DATE_FORMATTER.format(sysDate);
        }

        public static String getCurrentDateTime() {
            LocalDateTime sysDate = LocalDateTime.now();
            return DATETIME_FORMATTER.format(sysDate);
        }
    }
}

package testingmachine_backend.projects.metaWithProcess.Controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.controller.JsonController;

import java.time.Duration;

public class Config {

    public static final String BaseUrl = JsonController.getSystemURL();
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/";

    public static final int TIMEOUT = 10;

    public static WebDriverWait getWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }
}

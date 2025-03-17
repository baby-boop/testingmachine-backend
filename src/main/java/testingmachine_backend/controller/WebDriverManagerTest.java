package testingmachine_backend.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebDriverManagerTest {

    private static final String GRID_URL = "http://host.docker.internal:4444/";

    public static WebDriver getDriver() {
        try {
            ChromeOptions options = new ChromeOptions();

            // ✅ Браузерийн хувилбарыг хамгийн сүүлийн хувилбар дээр тохируулах
            options.setCapability("browserVersion", "latest");
            // ✅ Windows дээр ажиллуулах бол платформыг тохируулах
            options.setCapability("platformName", "Windows");
            options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));

            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            return new RemoteWebDriver(new URL(GRID_URL), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Selenium Grid рүү холбогдох URL буруу байна!", e);
        }
    }

    public static void quitDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}


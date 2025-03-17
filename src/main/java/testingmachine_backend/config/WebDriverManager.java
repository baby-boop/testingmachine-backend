package testingmachine_backend.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = ThreadLocal.withInitial(() -> {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu");
//        options.addArguments("--disable-gpu");
        return new ChromeDriver(options);
    });

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}

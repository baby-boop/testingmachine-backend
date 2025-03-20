package testingmachine_backend.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static testingmachine_backend.config.ConfigForAll.getLocalIpAddress;

public class WebDriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = ThreadLocal.withInitial(() -> {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu",
                "--headless",
                "--ignore-ssl-errors=yes",
                "--ignore-certificate-errors",
                "--disable-dev-shm-usage");
        options.setCapability("goog:loggingPrefs", Map.of(LogType.BROWSER, "ALL"));
        return new ChromeDriver(options);
    });

    public static WebDriver getDriverManager() {
        return driverThreadLocal.get();
    }

//    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
//
//    public static WebDriver getDriverManager() {
//        if (driverThreadLocal.get() == null) {
//            try {
//                String localIpAddress = getLocalIpAddress();
//
//                ChromeOptions options = new ChromeOptions();
//                options.addArguments(
//                        "--headless",
//                        "--disable-gpu",
//                        "--ignore-ssl-errors=yes",
//                        "--ignore-certificate-errors",
//                        "--disable-dev-shm-usage"
//                );
//                options.setCapability("goog:loggingPrefs", Map.of(LogType.BROWSER, "ALL"));
//
//                WebDriver driver = new RemoteWebDriver(new URL("http://" + localIpAddress + ":4444/wd/hub"), options);
//                driverThreadLocal.set(driver);
//
//            }catch(MalformedURLException e) {
//                throw new RuntimeException("Failed to connect to Selenium Grid", e);
//            }
//        }
//        return driverThreadLocal.get();
//    }

    public static void quitDriverManager() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}

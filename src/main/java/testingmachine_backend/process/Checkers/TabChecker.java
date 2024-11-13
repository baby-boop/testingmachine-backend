package testingmachine_backend.process.Checkers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TabChecker {

    public static boolean isTab(WebDriver driver, String id) {
        try {
            List<WebElement> tabs = driver.findElements(By.cssSelector(".bp-tabs .nav-tabs .nav-item .nav-link"));
            return !tabs.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}

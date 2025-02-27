package testingmachine_backend.projects.process.Checkers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LayoutChecker {

    public static boolean isLayout(WebDriver driver, String id) {
        try {
            WebElement isLayout = driver.findElement(By.cssSelector("div[class*='bp-layout-']"));
            return isLayout != null;
        } catch (Exception e) {
            return false;
        }
    }
}

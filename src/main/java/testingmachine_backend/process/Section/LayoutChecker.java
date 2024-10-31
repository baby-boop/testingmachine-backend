package testingmachine_backend.process.Section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LayoutChecker {

    public static boolean isLayout(WebDriver driver, String id) {
        try {
            WebElement isLayout = driver.findElement(By.cssSelector("div[class*='bp-layout-']"));
            return isLayout != null;
        } catch (Exception e) {
            System.out.println("Error layout: " + id );
            return false;
        }
    }
}

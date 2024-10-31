package testingmachine_backend.process.Section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LayoutDataChecker {

    public static boolean isHasData(WebDriver driver, String sectionCode) {
        try {
            WebElement isLayout = driver.findElement(By.cssSelector("div[data-section-code='"+ sectionCode +"'] .bp-detail-row"));
            return isLayout != null;
        } catch (Exception e) {
            return false;
        }
    }
}

package testingmachine_backend.process.Section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LayoutDataChecker {

    public static boolean isHasData(WebDriver driver, String sectionCode) {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("div[data-section-code='" + sectionCode + "'] .tbody .bp-detail-row"));
            return !rows.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}

package testingmachine_backend.process.Checkers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProcessWizardChecker {

    public static boolean isWizard(WebDriver driver, String id) {
        try {
            WebElement isWizard = driver.findElement(By.cssSelector("div[id*='wizard-']"));
            return isWizard != null;
        } catch (Exception e) {
            return false;
        }
    }
}

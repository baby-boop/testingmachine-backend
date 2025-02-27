package testingmachine_backend.projects.indicator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckListChecker {

    public static boolean isCheckList(WebDriver driver, String id) {
        try {
            WebElement isLayout = driver.findElement(By.cssSelector("div[id*='dialog-valuemap-']"));
            return isLayout != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBusinessProcess(WebDriver driver, String id) {
        try {
            WebElement isLayout = driver.findElement(By.cssSelector("div[id*='dialog-businessprocess-']"));
            return isLayout != null;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isWizard(WebDriver driver, String id) {
        try {
            WebElement isLayout = driver.findElement(By.cssSelector("div[id*='wizard-']"));
            return isLayout != null;
        }catch (Exception e) {
            return false;
        }
    }
}

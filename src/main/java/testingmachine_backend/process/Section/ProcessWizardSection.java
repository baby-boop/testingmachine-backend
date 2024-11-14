package testingmachine_backend.process.Section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;

public class ProcessWizardSection {

    private static final Logger LOGGER = Logger.getLogger(LayoutProcessSection.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;

    public static void KpiWizardFunction(WebDriver driver, String id, String fileName) {
        List<WebElement> wizardTabpanelPaths = findWizardTabpanelPath(driver, id);
        if(wizardTabpanelPaths != null){
            for (WebElement sectionPath : wizardTabpanelPaths) {
                String wizardTabId = sectionPath.getAttribute("id");
                waitUtils(driver);
                List<WebElement> wizardFieldPath = findWizardFieldPath(driver, id, wizardTabId);
                processTabElements(driver, wizardFieldPath, id, fileName);

                WebElement findNextButton = driver.findElement(By.cssSelector("div[id='bp-window-" + id + "'] li[aria-hidden='false'] a.btn.btn-primary"));
                findNextButton.click();
            }
        }else{
            LOGGER.log(Level.SEVERE, "No Wizard Tabpanel found");
        }
    }

    public static List<WebElement> findWizardTabpanelPath(WebDriver driver, String processId ) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            Thread.sleep(2000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + processId + "'] ")));
            return driver.findElements(By.cssSelector("div[id='bp-window-" + processId + "'] section[role='tabpanel']"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Wizard 'tabpanel' not found: " + processId);
            return null;
        }
    }
    public static  List<WebElement> findWizardFieldPath(WebDriver driver, String processId,  String wizardTabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + processId + "']")));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[id='bp-window-" + processId + "'] section[id='"+ wizardTabId +"'] ")));

            List<WebElement> tabElements = MainProcess.findElements(
                    By.cssSelector("input[data-path='kpiDmDtl.fact1']")
            );

            Map<String, WebElement> uniqueTabElements = new LinkedHashMap<>();
            for (WebElement tabElement : tabElements) {
                String tabDataPath = tabElement.getAttribute("name");
                if (tabDataPath != null &&  !uniqueTabElements.containsKey(tabDataPath)) {
                    uniqueTabElements.put(tabDataPath, tabElement);
                }
            }
            return new ArrayList<>(uniqueTabElements.values());
        }
        catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Elements with '"+ wizardTabId +"' not found");
            return Collections.emptyList();
        }
    }
}

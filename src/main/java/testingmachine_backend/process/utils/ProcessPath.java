package testingmachine_backend.process.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.*;
import testingmachine_backend.process.Checkers.LayoutChecker;
import testingmachine_backend.process.Messages.IsProcessMessage;
import testingmachine_backend.process.Section.LayoutProcessSection;
import testingmachine_backend.process.Checkers.ProcessWizardChecker;
import testingmachine_backend.process.Section.ProcessWizardSection;

import java.time.Duration;
import java.util.*;
import java.util.logging.*;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.DetailsFieldUtils.detailActionButton;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;
import static testingmachine_backend.process.utils.TabDetailsFieldUtils.tabDetailItems;

@Slf4j
public class ProcessPath {
    static final Logger LOGGER = Logger.getLogger(ProcessPath.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int LONG_WAIT_SECONDS = 90;
    @Getter
    private static final int failedCount = 0;

    public static void isProcessPersent(WebDriver driver, String id, String fileName) {
        try {
            waitUtils(driver);

            int maxAttempts = 2;
            for (int attempt = 0; attempt < maxAttempts; attempt++) {

                if (LayoutChecker.isLayout(driver, id)) {
                    LayoutProcessSection.LayoutFieldFunction(driver, id, fileName);
                } else if (ProcessWizardChecker.isWizard(driver, id)) {
                    ProcessWizardSection.KpiWizardFunction(driver, id, fileName);
                } else {
                    List<WebElement> elementsWithDataPath = findElementsWithSelector(driver, id);
                    processTabElements(driver, elementsWithDataPath, id, fileName);
                    tabDetailItems(driver, id, fileName);
                    waitUtils(driver);

                    detailActionButton(driver, id, fileName);
                    waitUtils(driver);
                }
                waitUtils(driver);
            }

            LOGGER.log(Level.INFO, "Process complete after " + maxAttempts + " attempts: " + id);

//            saveButtonFunction(driver, id);

            waitUtils(driver);
            if (IsProcessMessage.isErrorMessagePresent(driver, id, fileName)) {
                LOGGER.log(Level.INFO, "Process success: " + id);
            } else {
                LOGGER.log(Level.SEVERE, "Process failed: " + id);
            }

        }catch (NoSuchElementException n) {
            LOGGER.log(Level.SEVERE, "NoSuchElementException: " + id + n);
        } catch (TimeoutException t) {
            LOGGER.log(Level.SEVERE, "TimeoutException: " + id + t);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error process: " + id + e);
        }
    }

    private static void saveButtonFunction(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));
        try {
            WebElement wfmDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            WebElement wfmSaveButton = wfmDialog.findElement(By.xpath(".//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]"));
            wfmSaveButton.click();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Save button not found");
        }
    }

    public static List<WebElement> findElementsWithSelector(WebDriver driver,String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "'] .table-scrollable-borderless .bp-header-param")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
}







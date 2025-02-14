package testingmachine_backend.indicator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.Checkers.LayoutChecker;
import testingmachine_backend.process.Checkers.ProcessWizardChecker;
import testingmachine_backend.process.Messages.IsProcessMessage;
import testingmachine_backend.process.Section.LayoutProcessSection;
import testingmachine_backend.process.Section.ProcessWizardSection;
import testingmachine_backend.process.Service.ProcessMessageStatusService;
import testingmachine_backend.process.utils.ElementsFunctionUtils;
import testingmachine_backend.process.utils.ProcessPath;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.DetailsFieldUtils.detailActionButton;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.getUniqueTabElements;
import static testingmachine_backend.process.utils.TabDetailsFieldUtils.tabDetailItems;

@Slf4j
public class IndicatorPath {

    static final Logger LOGGER = Logger.getLogger(IndicatorPath.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int MEDIUM_WAIT_SECONDS = 5;

    @Getter
    private static int failedCount = 0;

    public static void isProcessPersent(WebDriver driver, String id, String systemName, String code, String name, String TestProcessType, String jsonId) {
        try {
            waitUtils(driver);

            consoleLogChecker(driver, id, systemName, jsonId);

            findMainProcessType(driver, id, systemName, jsonId);

//            waitUtils(driver);
//
//            if (!isDuplicateLogEntry(systemName, id, jsonId)) {
//                waitUtils(driver);
//                if(TestProcessType.contains("meta")){
//                    saveButtonFromMetaFunction(driver);
//                    checkMessageInfo(driver, id, systemName, code, name, TestProcessType, jsonId);
//                }else{
//                    saveButtonFunction(driver);
//                    checkMessageInfo(driver, id, systemName, code, name, TestProcessType, jsonId);
//                }
//
//            }else{
//                failedCount++;
//                LOGGER.log(Level.SEVERE, "Process failed with expression error: " + id);
//                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId);
//            }
            waitUtils(driver);

        }catch (NoSuchElementException n) {
            failedCount++;
            LOGGER.log(Level.SEVERE, "NoSuchElementException: " + id + n);
            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId);
        } catch (TimeoutException t) {
            failedCount++;
            LOGGER.log(Level.SEVERE, "TimeoutException: " + id + t);
            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId);
        } catch (Exception e) {
            failedCount++;
            LOGGER.log(Level.SEVERE, "Exception: " + id + e);
            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId);
        }
    }

    private static void checkMessageInfo(WebDriver driver, String id, String systemName, String code, String name, String TestProcessType, String jsonId) {
        waitUtils(driver);
        consoleLogRequiredPath(driver, id, systemName, jsonId);
        if (!IsProcessMessage.isErrorMessagePresent(driver, id, code, name, systemName, TestProcessType, jsonId)) {
            waitUtils(driver);
            failedCount++;
            LOGGER.log(Level.SEVERE, "Process failed with alert: " + id);
            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId);
        }
    }

    public static void findMainProcessType(WebDriver driver, String id, String systemName, String jsonId) {
//        if (LayoutChecker.isLayout(driver, id)) {
//            LayoutProcessSection.LayoutFieldFunction(driver, id, systemName, jsonId);
//        } else if (ProcessWizardChecker.isWizard(driver, id)) {
//            ProcessWizardSection.KpiWizardFunction(driver, id, systemName, jsonId);
//        } else {
        try {
            List<WebElement> findAddRowButtons = findAddRowButtons(driver);
            for (WebElement element : findAddRowButtons) {
                String sectionPath = element.getAttribute("data-action-path");
                List<WebElement> findActionPaths = findRowActionPathsButton(driver, sectionPath);
                for (WebElement findActionPath : findActionPaths) {
                    findActionPath.click();
                    Thread.sleep(1000);
                }
            }

            List<WebElement> elementsWithDataPath = findElementsWithSelector(driver);
            processTabElements(driver, elementsWithDataPath, id, systemName, jsonId);
//            tabDetailItems(driver, id, systemName, jsonId);
//            waitUtils(driver);
//            detailActionButton(driver, id, systemName, jsonId);
//            waitUtils(driver);
//        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isDuplicateLogEntry(String systemName, String id, String jsonId) {
        return ElementsFunctionUtils.ProcessLogFields.get().stream()
                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId));
    }


    public static List<WebElement> findElementsWithSelector(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table-scrollable-borderless .bp-header-param")));
//            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-17126382430219']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }


    public static List<WebElement> findAddRowButtons(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-17126382430219']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector(".mv-add-row-actions .bp-add-one-row:not([style*='display: none'])"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Elements 'data-action-path' not found");
            return List.of();
        }
    }

    public static List<WebElement> findRowActionPathsButton(WebDriver driver, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            String selector = String.format("button[data-action-path='%s']", sectionPath);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
            return driver.findElements(By.cssSelector(selector));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths: " + sectionPath);
            return null;
        }
    }
}

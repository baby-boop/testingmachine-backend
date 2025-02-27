package testingmachine_backend.projects.process.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.*;
import testingmachine_backend.projects.process.Checkers.LayoutChecker;

import testingmachine_backend.projects.process.Messages.IsProcessMessage;
import testingmachine_backend.projects.process.Section.LayoutProcessSection;
import testingmachine_backend.projects.process.Checkers.ProcessWizardChecker;
import testingmachine_backend.projects.process.Section.ProcessWizardSection;
import testingmachine_backend.projects.process.Service.ProcessMessageStatusService;

import java.time.Duration;
import java.util.*;
import java.util.logging.*;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.projects.process.utils.DetailsFieldUtils.detailActionButton;
import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;
import static testingmachine_backend.projects.process.utils.TabDetailsFieldUtils.tabDetailItems;

@Slf4j
public class ProcessPath {

    static final Logger LOGGER = Logger.getLogger(ProcessPath.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int MEDIUM_WAIT_SECONDS = 5;

    @Getter
    private static int failedCount = 0;

    public static void isProcessPersent(WebDriver driver, String id, String systemName, String code, String name, String TestProcessType, String jsonId) {
        try {
            waitUtils(driver);

            consoleLogChecker(driver, id, systemName, jsonId);

            findMainProcessType(driver, id, systemName, jsonId);

            waitUtils(driver);

            if (!isDuplicateLogEntry(systemName, id, jsonId)) {
                waitUtils(driver);
                if(TestProcessType.contains("meta")){
                    saveButtonFromMetaFunction(driver);
                    checkMessageInfo(driver, id, systemName, code, name, TestProcessType, jsonId);
                }else{
                    saveButtonFunction(driver);
                    checkMessageInfo(driver, id, systemName, code, name, TestProcessType, jsonId);
                }

            }else{
                failedCount++;
                LOGGER.log(Level.SEVERE, "Process failed with expression error: " + id);
                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId);
            }
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
        if (LayoutChecker.isLayout(driver, id)) {
            LayoutProcessSection.LayoutFieldFunction(driver, id, systemName, jsonId);
        } else if (ProcessWizardChecker.isWizard(driver, id)) {
            ProcessWizardSection.KpiWizardFunction(driver, id, systemName, jsonId);
        } else {
            List<WebElement> elementsWithDataPath = findElementsWithSelector(driver);
            processTabElements(driver, elementsWithDataPath, id, systemName, jsonId,"");
            tabDetailItems(driver, id, systemName, jsonId);
            waitUtils(driver);
            detailActionButton(driver, id, systemName, jsonId);
            waitUtils(driver);
        }
    }

    private static void saveButtonFunction(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT_SECONDS));
        try {

            Thread.sleep(2000);
            WebElement wfmSaveButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("button[onclick*='runSaveTestToolForm']")
            ));
            wfmSaveButton.sendKeys(" ");
        }catch (Exception e){
//
        }
    }

    private static void saveButtonFromMetaFunction(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT_SECONDS));
        try {

            Thread.sleep(2000);
            WebElement cnclBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@class, 'bp-btn-save') and text()='Хадгалах']")));
            cnclBtn.sendKeys(" ");

        }catch (Exception e){
//
        }
    }


    public static boolean isDuplicateLogEntry(String systemName, String id, String jsonId) {
        return ElementsFunctionUtils.ProcessLogFields.get().stream()
                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId));
    }


    public static List<WebElement> findElementsWithSelector(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table-scrollable-borderless .bp-header-param")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
}
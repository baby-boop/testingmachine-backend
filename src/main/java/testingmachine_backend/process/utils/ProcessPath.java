package testingmachine_backend.process.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.*;
import testingmachine_backend.process.Checkers.LayoutChecker;
import testingmachine_backend.process.DTO.NotFoundSaveButtonDTO;
import testingmachine_backend.process.Fields.NotFoundSaveButtonField;
import testingmachine_backend.process.Messages.IsProcessMessage;
import testingmachine_backend.process.Section.LayoutProcessSection;
import testingmachine_backend.process.Checkers.ProcessWizardChecker;
import testingmachine_backend.process.Section.ProcessWizardSection;
import testingmachine_backend.process.Service.ProcessMessageStatusService;

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

    @Getter
    private static int failedCount = 0;

    @NotFoundSaveButtonField
    public static final List<NotFoundSaveButtonDTO> notFoundField = new ArrayList<>();

    public static void isProcessPersent(WebDriver driver, String id, String fileName) {
        try {
            waitUtils(driver);
            consoleLogChecker(driver, id, fileName);
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

            if (!isDuplicateLogEntry(fileName, id)) {
                waitUtils(driver);
                saveButtonFunction(driver, id, fileName);
                consoleLogChecker(driver, id, fileName);
                if (!IsProcessMessage.isErrorMessagePresent(driver, id, fileName)) {
                    waitUtils(driver);
                    consoleLogChecker(driver, id, fileName);
                    failedCount++;
                    LOGGER.log(Level.SEVERE, "Process failed with alert: " + id);
                    ProcessMessageStatusService.addProcessStatus(fileName, id, "failed", "");
                }
            }else{
                failedCount++;
                LOGGER.log(Level.SEVERE, "Process failed with expression error: " + id);
                ProcessMessageStatusService.addProcessStatus(fileName, id, "failed", "");
            }
            waitUtils(driver);

        }catch (NoSuchElementException n) {
            failedCount++;
            LOGGER.log(Level.SEVERE, "NoSuchElementException: " + id + n);
            ProcessMessageStatusService.addProcessStatus(fileName, id, "failed", "");
        } catch (TimeoutException t) {
            failedCount++;
            LOGGER.log(Level.SEVERE, "TimeoutException: " + id + t);
            ProcessMessageStatusService.addProcessStatus(fileName, id, "failed", "");
        } catch (Exception e) {
            failedCount++;
            LOGGER.log(Level.SEVERE, "Exception: " + id + e);
            ProcessMessageStatusService.addProcessStatus(fileName, id, "failed", "");
        }
    }

    private static void saveButtonFunction(WebDriver driver, String id, String fileName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement wfmDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            WebElement wfmSaveButton = wfmDialog.findElement(By.xpath(".//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]"));
            wfmSaveButton.click();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Save button not found");
            NotFoundSaveButtonDTO notFoundFields = new NotFoundSaveButtonDTO(fileName, id);
            notFoundField.add(notFoundFields);
        }
    }
    public static boolean isDuplicateLogEntry(String fileName, String id) {
        return ElementsFunctionUtils.ProcessLogFields.stream()
                .anyMatch(log -> log.getFileName().equals(fileName) && log.getProcessId().equals(id));
    }

    public static List<NotFoundSaveButtonDTO> getProcessSaveMessages() {
        return new ArrayList<>(notFoundField);
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
package testingmachine_backend.indicator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.Messages.IsProcessMessage;
import testingmachine_backend.process.Messages.PopupMessage;
import testingmachine_backend.process.Service.ProcessMessageStatusService;
import testingmachine_backend.process.utils.ElementsFunctionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.getUniqueTabElements;

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

            checkMessageInfo(driver, id, systemName, code, name, TestProcessType, jsonId);

            waitUtils(driver);

            List<WebElement> headerTabs = findHeaderTabs(driver);
            if (!headerTabs.isEmpty()){
                for (WebElement headerTab : headerTabs) {
                    waitUtils(driver);
                    Thread.sleep(1000);
                    String headerTabText = headerTab.getText();
                    String tabId = headerTab.getAttribute("href");
                    if (tabId != null && tabId.contains("#")) {
                        tabId = tabId.split("#")[1];
                    }

                    waitUtils(driver);
                    headerTab.click();
                    waitUtils(driver);
                    List<WebElement> sideBars = findSideBarByTab(driver, tabId);
                    if(!sideBars.isEmpty()){
                        for (WebElement sideBarElement : sideBars) {
                            sideBarElement.click();
                            waitUtils(driver);
                            Thread.sleep(1000);
                            String sideBarText = sideBarElement.getText();
                            String stepId = sideBarElement.getAttribute("data-stepid");
                            String jsonData = sideBarElement.getAttribute("data-json");
                            consoleLogChecker(driver, stepId, sideBarText, jsonId);

                            if(jsonData != null){
                                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

                                long kpiTypeId = jsonObject.has("kpiTypeId") ? jsonObject.get("kpiTypeId").getAsLong() : 0L;
                                long metaDataId = jsonObject.has("metaDataId") && !jsonObject.get("metaDataId").isJsonNull() ? jsonObject.get("metaDataId").getAsLong() : 0L;
                                long metaTypeId = jsonObject.has("metaTypeId") && !jsonObject.get("metaTypeId").isJsonNull() ? jsonObject.get("metaTypeId").getAsLong() : 0L;
                                String typeCode = jsonObject.has("typeCode") && !jsonObject.get("typeCode").isJsonNull() ? jsonObject.get("typeCode").getAsString() : "null";

                                if(kpiTypeId != 0L && metaDataId == 0L){
                                    if(kpiTypeId == 2008L){

//                                        List<WebElement> findAddRowButtons = findAddRowButtonBySidebar(driver, stepId);
//                                        findRow(driver, findAddRowButtons);
//                                        findAddRowButtons.clear();

                                        List<WebElement> dataPathBySidebars = findDataPathBySidebar(driver, stepId);
                                        processTabElements(driver, dataPathBySidebars, id, systemName, jsonId);
                                        dataPathBySidebars.clear();

                                        consoleLogChecker(driver, stepId, sideBarText, jsonId);

                                        waitUtils(driver);
                                        SideBarSaveButton(driver, stepId);
                                        waitUtils(driver);

                                        Thread.sleep(1000);
                                        consoleLogRequiredPath(driver, stepId, sideBarText, jsonId);
                                        if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, stepId, headerTabText, sideBarText, "METHOD", jsonId)) {
                                            waitUtils(driver);

                                            IndicatorCustomTab customTab = new IndicatorCustomTab(id, stepId, headerTabText, sideBarText, "METHOD", "failed", "Алдаа гарлаа", jsonId,
                                                    ElementsFunctionUtils.getProcessLogMessages()
                                                            .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
                                                    ElementsFunctionUtils.getUniqueEmptyDataPath()
                                                            .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
                                                    PopupMessage.getUniquePopupMessages()
                                                            .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
                                                    ElementsFunctionUtils.getPopupStandartMessages()
                                                            .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
                                                    ElementsFunctionUtils.getRequiredPathMessages()
                                                            .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
                                                    ElementsFunctionUtils.getComboMessages()
                                                            .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()));
                                            IsIndicatorMessage.addIndicatorMessage(customTab);

                                            LOGGER.log(Level.SEVERE, "Process failed with alert: " + id + "  stepid: " + stepId);
                                        }

                                    }else if(kpiTypeId == 16641793815766L){
                                        System.out.println("stepId: " + stepId + "   sideBarText: "+ sideBarText + "  headerTabText: " + headerTabText);
                                    }
                                }else if(kpiTypeId == 0L && metaTypeId != 0L){
                                    System.out.println("Metadata daraa ni hiinee");
                                }
                            }
                        }
                    }else{
                        List<WebElement> findAddRowButtons = findAddRowButtonsFromTab(driver, tabId);
                        findRow(driver, findAddRowButtons);
                        findAddRowButtons.clear();

                        List<WebElement> elementsWithDataPath = findElementsWithSelectorFromTab(driver, tabId);
                        processTabElements(driver, elementsWithDataPath, id, systemName, jsonId);
                        elementsWithDataPath.clear();

//                        CustomTabSaveButton(driver, tabId);
                        waitUtils(driver);

                        if (IsIndicatorMessage.isErrorMessagePresent(driver, systemName, id, headerTabText, "test", "process", jsonId)) {
                            waitUtils(driver);
                        }
                    }
                }
                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "success", "Амжилттай хадгалагдлаа", TestProcessType, jsonId);
            }

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

            if(CheckListChecker.isCheckList(driver, id)){
//                List<WebElement> findAddRowButtons = findAddRowButtons1(driver, id);
//                findRow(driver, findAddRowButtons);
//                findAddRowButtons.clear();

                List<WebElement> elementsWithDataPath = findElementsWithSelector1(driver, id);
                processTabElements(driver, elementsWithDataPath, id, systemName, jsonId);
                elementsWithDataPath.clear();

                waitUtils(driver);
                MainSaveButton(driver);
                waitUtils(driver);

            }else {

                List<WebElement> findAddRowButtons = findAddRowButtons2(driver, id);
                findRow(driver, findAddRowButtons);
                findAddRowButtons.clear();

                List<WebElement> elementsWithDataPath = findElementsWithSelector2(driver, id);
                processTabElements(driver, elementsWithDataPath, id, systemName, jsonId);
                elementsWithDataPath.clear();

                waitUtils(driver);
                MainSaveButton(driver);
                waitUtils(driver);

            }


//            tabDetailItems(driver, id, systemName, jsonId);
//            waitUtils(driver);
//            detailActionButton(driver, id, systemName, jsonId);
//            waitUtils(driver);
//        }

    }

    public static void MainSaveButton(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='mv-checklist-main-render']//button[contains(@class, 'bp-btn-save')]")));
            saveButton.sendKeys(" ");

            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error MainSaveButton");
        }
    }

    public static void CustomTabSaveButton(WebDriver driver, String tabid) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='" + tabid + "']//button[contains(@class, 'bp-btn-save')]")
            ));
            saveButton.click();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CustomTabSaveButton", e);
        }
    }

    public static void SideBarSaveButton(WebDriver driver, String stepid) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='mv_checklist_id_" + stepid + "']//button[contains(@class, 'bp-btn-save')]")
            ));
            saveButton.click();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CustomTabSaveButton", e);
        }
    }


//    Tab дотроос sidebar олох
    public static List<WebElement> findSideBarByTab(WebDriver driver, String tabId ) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='" + tabId +"'] .nav-sidebar")));
            List<WebElement> elements = MainTab.findElements(By.cssSelector(".nav-item .nav-link"));
            return new ArrayList<>(elements);
        }catch (NoSuchElementException n){
            LOGGER.log(Level.SEVERE, "NoSuchElementException findSideBarByTab");
            return List.of();
        }catch (TimeoutException t) {
            LOGGER.log(Level.SEVERE, "TimeoutException findSideBarByTab");
            return List.of();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error findSideBarByTab");
            return List.of();
        }
    }


    public static List<WebElement> findHeaderTabs(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mv-checklist2-render-parent .mv-checklist-tab")));
            List<WebElement> elements = MainTab.findElements(By.cssSelector(".mv-checklist-tab-link"));
            return new ArrayList<>(elements);
        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error findHeaderTabs");
            return List.of();
        }
    }

    private static void findRow(WebDriver driver, List<WebElement> findAddRowButtons)  {
        try{
            for (WebElement element : findAddRowButtons) {
                String sectionPath = element.getAttribute("data-action-path");
                List<WebElement> findActionPaths = findRowActionPathsButton(driver, sectionPath);
                for (WebElement findActionPath : findActionPaths) {
                    findActionPath.click();
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error findRow", e);
        }
    }

    public static boolean isDuplicateLogEntry(String systemName, String id, String jsonId) {
        return ElementsFunctionUtils.ProcessLogFields.get().stream()
                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId));
    }


    public static List<WebElement> findElementsWithSelector1(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }

    public static List<WebElement> findDataPathBySidebar(WebDriver driver, String stepId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + stepId +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }

    public static List<WebElement> findElementsWithSelector2(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-" + id +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }

    public static List<WebElement> findAddRowButtonsFromTab(WebDriver driver, String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='"+ tabId +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector(".mv-add-row-actions .bp-add-one-row:not([style*='display: none'])"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findAddRowButtonsFromTab on Elements 'data-action-path' not found");
            return List.of();
        }
    }

    //SIDEBAR add row ажлуулна
    public static List<WebElement> findAddRowButtonBySidebar(WebDriver driver, String stepId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + stepId +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector(".mv-add-row-actions .bp-add-one-row:not([style*='display: none'])"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findAddRowButtonsFromTab on Elements 'data-action-path' not found");
            return List.of();
        }
    }


    public static List<WebElement> findElementsWithSelectorFromTab(WebDriver driver, String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='" + tabId +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }


    public static List<WebElement> findAddRowButtons1(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-"+ id +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector(".mv-add-row-actions .bp-add-one-row:not([style*='display: none'])"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findAddRowButtons1 Elements 'data-action-path' not found");
            return List.of();
        }
    }

    public static List<WebElement> findAddRowButtons2(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-"+ id +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector(".mv-add-row-actions .bp-add-one-row:not([style*='display: none'])"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findAddRowButtons2 on Elements 'data-action-path' not found");
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

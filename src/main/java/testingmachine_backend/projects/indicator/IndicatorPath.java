//package testingmachine_backend.projects.indicator;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.*;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import testingmachine_backend.projects.process.Messages.IsProcessMessage;
//import testingmachine_backend.projects.process.Messages.PopupMessage;
//import testingmachine_backend.projects.process.Service.ProcessMessageStatusService;
//import testingmachine_backend.projects.process.utils.ElementsFunctionUtils;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//
//import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;
//import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;
//
//@Slf4j
//public class IndicatorPath {
//
//    static final Logger LOGGER = Logger.getLogger(IndicatorPath.class.getName());
//    private static final int SHORT_WAIT_SECONDS = 2;
////    private static final int MEDIUM_WAIT_SECONDS = 5;
//
//    @Getter
//    private static int failedCount = 0;
//
//    public static void isProcessPersent(WebDriver driver, String id, String systemName, String code, String name, String TestProcessType, String jsonId, int totalCount) {
//        try {
//            waitUtils(driver);
//
//            consoleLogChecker(driver, id, systemName, jsonId);
//
//            findMainProcessType(driver, systemName, id, code, name, TestProcessType, jsonId, totalCount);
//
//            waitUtils(driver);
//
//        }catch (NoSuchElementException n) {
//            failedCount++;
//            LOGGER.log(Level.SEVERE, "NoSuchElementException: " + id + n);
//            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId, totalCount);
//        } catch (TimeoutException t) {
//            failedCount++;
//            LOGGER.log(Level.SEVERE, "TimeoutException: " + id + t);
//            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId, totalCount);
//        } catch (Exception e) {
//            failedCount++;
//            LOGGER.log(Level.SEVERE, "Exception: " + id + e);
//            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId, totalCount);
//        }
//    }
//
//    public static void findMainProcessType(WebDriver driver, String systemName, String id, String code, String name, String type,  String jsonId, int totalCount) {
//        if(CheckListChecker.isCheckList(driver, id)){
//            List<WebElement> sections = findSectionsGroupMain(driver, id);
//            if(!sections.isEmpty()){
//                clickAddRowButtons(sections);
//                sections.clear();
//            }
//
//            List<WebElement> elementsWithDataPath = findElementsWithDialog(driver, id);
//            processTabElements(driver, elementsWithDataPath, id, systemName, jsonId, "");
//            elementsWithDataPath.clear();
//
//            consoleLogChecker(driver, id, systemName, jsonId);
//            if (!isDuplicateLogEntry(systemName, id, jsonId)) {
//                waitUtils(driver);
//                MainSaveButton(driver, id);
//                waitUtils(driver);
//
//                checkMessageInfo(driver, id, systemName, code, name, type, jsonId, totalCount);
//                findAndWorkingSiderTabs(driver, systemName, id, code, name, type, jsonId);
//
//            } else{
//                failedCount++;
//                LOGGER.log(Level.SEVERE, "Process failed with expression error: " + id);
//                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", type, jsonId, totalCount);
//            }
//        }else if(CheckListChecker.isBusinessProcess(driver, id)){
//
//            List<WebElement> sections = findSectionsGroupMainBp(driver, id);
//            if(!sections.isEmpty()){
//                clickAddRowButtons(sections);
//                sections.clear();
//            }
//
//            List<WebElement> elementsWithDataPath = findElementsWithBp(driver, id);
//            processTabElements(driver, elementsWithDataPath, id, systemName, jsonId, "");
//            elementsWithDataPath.clear();
//
//            consoleLogChecker(driver, id, systemName, jsonId);
//            if (!isDuplicateLogEntry(systemName, id, jsonId)) {
//                waitUtils(driver);
//                MainSaveButtonBp(driver, id);
//                waitUtils(driver);
//
//                checkMessageInfo(driver, id, systemName, code, name, type, jsonId, totalCount);
//            }else{
//                failedCount++;
//                LOGGER.log(Level.SEVERE, "Process failed with expression error: " + id);
//                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", type, jsonId, totalCount);
//                MainCloseButtonBp(driver, id);
//            }
//
//        }
//    }
//
//    private static void checkMessageInfo(WebDriver driver, String id, String systemName, String code, String name, String TestProcessType, String jsonId, int totalCount) {
//        waitUtils(driver);
//        consoleLogRequiredPath(driver, id, systemName, jsonId);
//        if (!IsProcessMessage.isErrorMessagePresent(driver, id, code, name, systemName, TestProcessType, jsonId, totalCount)) {
//            waitUtils(driver);
//            failedCount++;
//            LOGGER.log(Level.SEVERE, "Process failed with alert: " + id);
//            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "", TestProcessType, jsonId, totalCount);
//        }
//    }
//
//    public static void findAndWorkingSiderTabs(WebDriver driver, String systemName, String id,  String code, String name, String TestProcessType, String jsonId) {
//        try{
//            List<WebElement> headerTabs = findHeaderTabs(driver);
//            if (!headerTabs.isEmpty()){
//                for (WebElement headerTab : headerTabs) {
//                    waitUtils(driver);
//                    Thread.sleep(1000);
//                    String headerTabText = headerTab.getText();
//                    String tabId = headerTab.getAttribute("href");
//                    if (tabId != null && tabId.contains("#")) {
//                        tabId = tabId.split("#")[1];
//                    }
//
//                    waitUtils(driver);
//                    headerTab.click();
//                    waitUtils(driver);
//                    List<WebElement> sideBars = findSideBarByTab(driver, tabId);
//                    if(!sideBars.isEmpty()){
//                        for (WebElement sideBarElement : sideBars) {
//                            sideBarElement.click();
//                            waitUtils(driver);
//                            Thread.sleep(1000);
//                            String sideBarText = sideBarElement.getText();
//                            String stepId = sideBarElement.getAttribute("data-stepid");
//                            String jsonData = sideBarElement.getAttribute("data-json");
//                            consoleLogChecker(driver, stepId, sideBarText, jsonId);
//
//                            if(jsonData != null){
//                                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
//
//                                long kpiTypeId = jsonObject.has("kpiTypeId") ? jsonObject.get("kpiTypeId").getAsLong() : 0L;
//                                long metaDataId = jsonObject.has("metaDataId") && !jsonObject.get("metaDataId").isJsonNull() ? jsonObject.get("metaDataId").getAsLong() : 0L;
//                                long metaTypeId = jsonObject.has("metaTypeId") && !jsonObject.get("metaTypeId").isJsonNull() ? jsonObject.get("metaTypeId").getAsLong() : 0L;
//                                String typeCode = jsonObject.has("typeCode") && !jsonObject.get("typeCode").isJsonNull() ? jsonObject.get("typeCode").getAsString() : "null";
//
//                                if(kpiTypeId != 0L && metaDataId == 0L){
//                                    if(kpiTypeId == 2008L){
//                                        List<WebElement> sections = findSectionsGroupTab(driver, stepId);
//                                        if(!sections.isEmpty()){
//                                            clickAddRowButtons(sections);
//                                            sections.clear();
//                                        }
//
//                                        List<WebElement> dataPathBySidebars = findDataPathBySidebar(driver, stepId);
//                                        processTabElements(driver, dataPathBySidebars, stepId, sideBarText, jsonId, "sidebar");
//
//                                        dataPathBySidebars.clear();
//
//                                        consoleLogChecker(driver, stepId, sideBarText, jsonId);
//                                        if (!isDuplicateLogEntry(sideBarText, stepId, jsonId)) {
//                                            waitUtils(driver);
//                                            SideBarSaveButton(driver, stepId);
//                                            waitUtils(driver);
//
//                                            Thread.sleep(1000);
//                                            consoleLogRequiredPath(driver, stepId, sideBarText, jsonId);
//                                            if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, stepId, headerTabText, sideBarText, "METHOD", jsonId)) {
//                                                waitUtils(driver);
//
//                                                IndicatorCustomTab customTab = createIndicatorCustomTab(id, stepId, headerTabText, sideBarText, "METHOD", jsonId);
//                                                IsIndicatorMessage.addIndicatorMessage(customTab);
//
//                                                LOGGER.log(Level.SEVERE, "Process failed with alert: " + id + "  stepid: " + stepId);
//                                            }
//                                        }else {
//                                            IndicatorCustomTab customTab = createIndicatorCustomTab(driver, id, stepId, headerTabText, sideBarText, "METHOD", jsonId);
//                                            IsIndicatorMessage.addIndicatorMessage(customTab);
//                                        }
//
//                                    }else if(kpiTypeId == 16641793815766L){
//
//                                        String createIndicatorId = findCreateIndicator(driver, stepId);
//
//                                        List<WebElement> sections = findSectionsGroupMain(driver, createIndicatorId);
//                                        if(!sections.isEmpty()){
//                                            clickAddRowButtons(sections);
//                                            sections.clear();
//                                        }
//
//                                        List<WebElement> elementsWithDataPath = findElementsWithDialog(driver, createIndicatorId);
//                                        processTabElements(driver, elementsWithDataPath, createIndicatorId, sideBarText, jsonId, "indicator");
//                                        elementsWithDataPath.clear();
//
//                                        consoleLogChecker(driver, createIndicatorId, sideBarText, jsonId);
//                                        if (!isDuplicateLogEntry(sideBarText, createIndicatorId, jsonId)) {
//
//                                            waitUtils(driver);
//                                            MainSaveButtonCreate(driver, createIndicatorId);
//                                            waitUtils(driver);
//
//                                            Thread.sleep(1000);
//                                            consoleLogRequiredPath(driver, createIndicatorId, sideBarText, jsonId);
//                                            if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, createIndicatorId, headerTabText, sideBarText, "LIST", jsonId)) {
//                                                waitUtils(driver);
//
//                                                IndicatorCustomTab customTab = createIndicatorCustomTab(id, createIndicatorId, headerTabText, sideBarText, "LIST", jsonId);
//                                                IsIndicatorMessage.addIndicatorMessage(customTab);
//
//                                            }
//                                        }
//                                        else {
//                                            IndicatorCustomTab customTab = createIndicatorCustomTab(id, stepId, headerTabText, sideBarText, "METHOD", jsonId);
//                                            IsIndicatorMessage.addIndicatorMessage(customTab);
//                                        }
//
//                                        clickReturnButton(driver, createIndicatorId);
//
//                                    }
//                                }else if(kpiTypeId == 0L && metaTypeId != 0L){
//                                    System.out.println("Metadata daraa ni hiinee");
//                                }
//                            }
//                        }
//                    }
//                    else{
//
//                        String indicatorId = findIndicatorByTab(driver, tabId);
//
//                        List<WebElement> elementsWithDataPath = findDataPathBySidebar(driver, indicatorId);
//                        processTabElements(driver, elementsWithDataPath, indicatorId, headerTabText, jsonId, "tab");
//                        elementsWithDataPath.clear();
//
//                        consoleLogChecker(driver, indicatorId, headerTabText, jsonId);
//                        if (!isDuplicateLogEntry(headerTabText, indicatorId, jsonId)) {
//
//                            waitUtils(driver);
//                            SideBarSaveButton(driver, indicatorId);
//                            waitUtils(driver);
//
//                            consoleLogRequiredPath(driver, indicatorId, headerTabText, jsonId);
//                            if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, indicatorId,  headerTabText, groupName, "", "METHOD", jsonId)) {
//                                waitUtils(driver);
//
//                                IndicatorCustomTab customTab = createIndicatorCustomTab(id, indicatorId, headerTabText, groupName, "", "METHOD", jsonId);
//                                IsIndicatorMessage.addIndicatorMessage(customTab);
//
//                            }
//                        }else{
//                            IndicatorCustomTab customTab = createIndicatorCustomTab(id, indicatorId, headerTabText, groupName, "", "METHOD", jsonId);
//                            IsIndicatorMessage.addIndicatorMessage(customTab);
//                        }
//                    }
//                }
//                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "success", "Амжилттай хадгалагдлаа", TestProcessType, jsonId);
//            }
//        }catch (Exception e){
//
//        }
//    }
//
//    //CheckList үед буцах товчийг дарна
//    private static void clickReturnButton(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));
//            WebElement closeButton = MainProcess.findElement(
//                    By.cssSelector("div a[onclick='checklistCloseDialog(this)']")
//            );
//            if(closeButton != null){
//                closeButton.click();
//            }
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error clickReturnButton {0}", id);
//        }
//    }
//
//    private static IndicatorCustomTab createIndicatorCustomTab(String id, String stepId, String headerTabText, String groupName, String sideBarText, String indicatorType, String jsonId) {
//        return new IndicatorCustomTab(
//                id, stepId, headerTabText, groupName, sideBarText, indicatorType, "failed", "Алдаа гарлаа", jsonId,
//                ElementsFunctionUtils.getProcessLogMessages()
//                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
//                ElementsFunctionUtils.getUniqueEmptyDataPath()
//                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
//                PopupMessage.getUniquePopupMessages()
//                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
//                ElementsFunctionUtils.getPopupStandartMessages()
//                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
//                ElementsFunctionUtils.getRequiredPathMessages()
//                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList()),
//                ElementsFunctionUtils.getComboMessages()
//                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList())
//        );
//    }
//
//    public static void MainSaveButton(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));
//            WebElement saveButton = MainProcess.findElement(By.xpath("//div[@class='mv-checklist-main-render']//button[contains(@class, 'btn-success bp-btn-save')]"));
//            if(saveButton != null){
//                saveButton.sendKeys(" ");
//            }
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error MainSaveButton {0}", id);
//        }
//    }
//
//    public static void MainSaveButtonBp(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-describedby='dialog-businessprocess-" + id +"']")));
//            WebElement saveButton = MainProcess.findElement(By.xpath(".//button[contains(@class, 'bp-btn-save') and text()='Хадгалах']"));
//            if(saveButton != null){
//                saveButton.sendKeys(" ");
//            }
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error MainSaveButtonBp {0}", id);
//        }
//    }
//    public static void MainCloseButtonBp(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-describedby='dialog-businessprocess-" + id +"']")));
//            WebElement saveButton = MainProcess.findElement(By.xpath(".//button[contains(@class, 'bp-btn-close') and text()='Хаах']"));
//            if(saveButton != null){
//                saveButton.sendKeys(" ");
//            }
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error MainSaveButtonBp {0}", id);
//        }
//    }
//
//
//    public static void MainSaveButtonCreate(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));
//            List<WebElement> saveButtons = MainProcess.findElements(
//                    By.xpath("//div[@class='mv-checklist-main-render']//button[contains(@class, 'btn-success bp-btn-save')]")
//            );
//
//            int buttonCount = saveButtons.size();
//
//            if (buttonCount > 0) {
//                WebElement lastSaveButton = saveButtons.get(buttonCount - 1);
//                lastSaveButton.sendKeys(" ");
//            } else {
//                LOGGER.log(Level.WARNING, "MainSaveButton1 Found in dialog-valuemap-{0}", id);
//            }
//            Thread.sleep(1000);
//
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error MainSaveButtonCreate {0}", id);
//        }
//    }
//
//    public static void SideBarSaveButton(WebDriver driver, String stepid) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//            WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//*[@id='mv_checklist_id_" + stepid + "']//button[contains(@class, 'bp-btn-save')]")
//            ));
//            saveButton.click();
//
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error in CustomTabSaveButton", e);
//        }
//    }
//
//    //Section дотор add-row байвал click хийнэ checklist
//    public static void clickAddRowButtons(List<WebElement> sections) {
//        for (WebElement section : sections) {
//            try {
//                String sectionPath = section.getAttribute("data-section-path");
//                List<WebElement> addRowActions = section.findElements(By.cssSelector("div.mv-add-row-actions"));
//
//                for (WebElement addRowAction : addRowActions) {
//                    try {
//                        WebElement addButton = addRowAction.findElement(By.cssSelector("button.bp-add-one-row"));
//                        if (addButton.isDisplayed() && addButton.isEnabled()) {
//                            addButton.click();
////                            LOGGER.log(Level.INFO, "Clicked add row button in section: {0}", sectionPath);
//                        }
//                    } catch (NoSuchElementException e) {
//                        LOGGER.log(Level.WARNING, "No add button found in section: {0}", sectionPath);
//                    }
//                }
//            } catch (Exception e) {
//                LOGGER.log(Level.SEVERE, "Error while clicking add row buttons", e);
//            }
//        }
//    }
//
//    //    Tab дотроос sidebar олох checklist
//    public static List<WebElement> findSideBarByTab(WebDriver driver, String tabId ) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='" + tabId +"'] .nav-sidebar")));
//            List<WebElement> elements = MainTab.findElements(By.cssSelector(".nav-item .nav-link"));
//            return new ArrayList<>(elements);
//        }catch (NoSuchElementException n){
//            LOGGER.log(Level.WARNING, "NoSuchElementException findSideBarByTab");
//            return List.of();
//        }catch (TimeoutException t) {
//            LOGGER.log(Level.WARNING, "TimeoutException findSideBarByTab");
//            return List.of();
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error findSideBarByTab");
//            return List.of();
//        }
//    }
//
//    //checklist
//    public static List<WebElement> findHeaderTabs(WebDriver driver) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mv-checklist2-render-parent .mv-checklist-tab")));
//            List<WebElement> elements = MainTab.findElements(By.cssSelector(".mv-checklist-tab-link"));
//            return new ArrayList<>(elements);
//        }catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error findHeaderTabs");
//            return List.of();
//        }
//    }
//
//    public static boolean isDuplicateLogEntry(String systemName, String id, String jsonId) {
//        return ElementsFunctionUtils.ProcessLogFields.get().stream()
//                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId));
//    }
//
//    //Metaverse datapath олох checklist
//    public static List<WebElement> findElementsWithDialog(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));
//
//            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
//            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);
//
//            return new ArrayList<>(uniqueDataPathElements.values());
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Elements with selector not found");
//            return List.of();
//        }
//    }
//
//    //датапат олох sidebar checklist
//    public static List<WebElement> findDataPathBySidebar(WebDriver driver, String stepId) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + stepId +"']")));
//
//            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
//            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);
//
//            return new ArrayList<>(uniqueDataPathElements.values());
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Elements with selector not found");
//            return List.of();
//        }
//    }
//
//    //Бүх олдож буй section-олох checklist
//    public static List<WebElement> findSectionsGroupMain(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-"+ id +"']")));
//                List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));
//
//            return new ArrayList<>(elements);
//        } catch (Exception e) {
//            LOGGER.log(Level.WARNING, "findSectionsGroup Elements 'data-action-path' not found {0}", id);
//            return List.of();
//        }
//    }
//
//    //Бүх олдож буй section-олох bp
//    public static List<WebElement> findSectionsGroupMainBp(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-"+ id +"']")));
//            List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));
//
//            return new ArrayList<>(elements);
//        } catch (Exception e) {
//            LOGGER.log(Level.WARNING, "findSectionsGroupMainBp Elements 'data-action-path' not found {0}", id);
//            return List.of();
//        }
//    }
//
//    //Metaverse datapath олох checklist
//    public static List<WebElement> findElementsWithBp(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-" + id +"']")));
//
//            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
//            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);
//
//            return new ArrayList<>(uniqueDataPathElements.values());
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "findElementsWithBp element with selector not found {0}", id);
//            return List.of();
//        }
//    }
//
//    // Tab дотор ашигласан section-г олох checklist
//    public static List<WebElement> findSectionsGroupTab(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//        try {
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_"+ id +"']")));
//            List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));
//
//            return new ArrayList<>(elements);
//        } catch (Exception e) {
//            LOGGER.log(Level.WARNING, "findSectionsGroupTab Elements 'data-action-path' not found {0}", id);
//            return List.of();
//        }
//    }
//
//    //Metaverse лист байвал нэмэх процессийг хайж ажлуулах checklist
//    public static String findCreateIndicator(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_"+ id +"'] .btn-group-devided")));
//            List<WebElement> links = MainProcess.findElements(By.tagName("a"));
//
//            if (!links.isEmpty()) {
//                for(WebElement link : links) {
//                    String actionType = link.getAttribute("data-actiontype");
//                    String createIndicatorId = link.getAttribute("data-main-indicatorid");
//                    if(actionType.equals("create")) {
//                        link.click();
//
//                        return createIndicatorId;
//                    }else {
//                        LOGGER.log(Level.WARNING, "Not found create mv_checklist_id_{0}", id);
//                    }
//                }
//            }
//        }catch(Exception e){
//            LOGGER.log(Level.WARNING, "findCreateIndicator element not found {0}", id);
//        }
//        return "";
//    }
//
//    //   sidebar indicatorId олох checklist
//    public static String findIndicatorByTab(WebDriver driver, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//
//        try{
//            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='"+ id +"']")));
//            WebElement indicatorElement = MainProcess.findElement(By.cssSelector(".checklist2-content-section .mv_checklist_render_all"));
//
//            if (indicatorElement != null ) {
//                String indicatorId = indicatorElement.getAttribute("id");
//                String replaceId = indicatorId.replace("mv_checklist_id_", "");
//                return replaceId;
//            }
//        }catch(Exception e){
//            LOGGER.log(Level.WARNING, "findCreateIndicator element not found {0}", id);
//        }
//        return "";
//    }
//}

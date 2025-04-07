package testingmachine_backend.projects.product;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.controller.IndicatorCallMethod;
import testingmachine_backend.projects.indicator.IndicatorCustomTab;
import testingmachine_backend.projects.indicator.IsIndicatorMessage;
import testingmachine_backend.projects.process.Messages.PopupMessage;
import testingmachine_backend.projects.process.Service.ProcessMessageStatusService;
import testingmachine_backend.projects.process.utils.ElementsFunctionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtilsProduct;
import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;

public class ProductTest {

    private static final int SHORT_WAIT_SECONDS = 2;
    static final Logger LOGGER = Logger.getLogger(ProductTest.class.getName());


    //<editor-fold defaultstate="collapsed" desc="Main function">
    public static void findAndWorkingSiderTabsTest(WebDriver driver, String systemName, String id, String code, String name, String TestProcessType, String jsonId, int totalCount) {

        try {

            /** header tab олох*/
            List<WebElement> headerTabs = findHeaderTabs(driver);
            if (!headerTabs.isEmpty()) {
                for (WebElement headerTab : headerTabs) {
                    waitUtilsProduct(driver);
                    Thread.sleep(1000);
                    String headerTabText = headerTab.getText();
                    String tabId = headerTab.getAttribute("href");
                    if (tabId != null && tabId.contains("#")) {
                        tabId = tabId.split("#")[1];
                    }
                    waitUtilsProduct(driver);
                    String testTabName = ProductConfigForTest.testTabName();

                    if (headerTabText.equals(testTabName) || testTabName == null || testTabName.isEmpty()) {

                        headerTab.click();
                        waitUtils(driver);

                        /** Бүх group олох */
                        List<WebElement> sideBarGroupElements = findSideBarByTabGroup(driver, tabId);
                        if (!sideBarGroupElements.isEmpty()) {
                            for (WebElement sideBarGroupElement : sideBarGroupElements) {

                                /** Group нэр олох клик хийх */
                                WebElement menuName = findSubMenuName(sideBarGroupElement);
                                assert menuName != null;
                                String groupName = menuName.getText();

                                String testGroupName = ProductConfigForTest.testGroupName();
                                if (groupName.equals(testGroupName) || testGroupName == null || testGroupName.isEmpty()) {

                                    if (menuName != null) {

                                        String elementClass = sideBarGroupElement.getAttribute("class");

                                        if (!elementClass.contains("nav-group-sub-mv-opened")) {

                                            menuName.click();
                                            waitUtilsProduct(driver);
                                        } else {
                                            System.out.println("Not clicked menuName: " + menuName.getText());
                                        }
                                    }

                                    /** group доторх sideBar олох */
                                    List<WebElement> sideBarGroup = findSubMenuItems(sideBarGroupElement);
                                    if (!sideBarGroup.isEmpty()) {
                                        for (WebElement sideBarElement : sideBarGroup) {
                                            String sideBarText = sideBarElement.getText();

                                            String testSideBarName = ProductConfigForTest.testSideBarName();
                                            if (sideBarText.equals(testSideBarName) || testSideBarName == null || testSideBarName.isEmpty()) {

                                                String notEqualSideBar = ProductConfigForTest.testNotEqualSideBarName();
                                                if (!sideBarText.equals(notEqualSideBar) || notEqualSideBar == null || notEqualSideBar.isEmpty()) {

                                                    sideBarElement.click();
                                                    waitUtilsProduct(driver);

                                                    String stepId = sideBarElement.getAttribute("data-stepid");
                                                    String jsonData = sideBarElement.getAttribute("data-json");
//                                                consoleLogChecker(driver, stepId, sideBarText, jsonId);

                                                    if (jsonData != null) {

                                                        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

                                                        long kpiTypeId = (jsonObject.has("kpiTypeId") && !jsonObject.get("kpiTypeId").isJsonNull())
                                                                ? jsonObject.get("kpiTypeId").getAsLong()
                                                                : 0L;
                                                        String metaDataId = (jsonObject.has("metaDataId") && !jsonObject.get("metaDataId").isJsonNull())
                                                                ? jsonObject.get("metaDataId").getAsString()
                                                                : "";
                                                        long metaTypeId = (jsonObject.has("metaTypeId") && !jsonObject.get("metaTypeId").isJsonNull())
                                                                ? jsonObject.get("metaTypeId").getAsLong()
                                                                : 0L;
                                                        String indicatorId = (jsonObject.has("indicatorId") && !jsonObject.get("indicatorId").isJsonNull())
                                                                ? jsonObject.get("indicatorId").getAsString()
                                                                : "";

                                                        if (kpiTypeId == 2008L) {
                                                            List<WebElement> sections = findSectionsGroupTab(driver, stepId);
                                                            if (!sections.isEmpty()) {
                                                                clickAddRowButtons(sections);
                                                                sections.clear();
                                                            }

                                                            List<WebElement> dataPathBySidebars = findDataPathBySidebar(driver, stepId);
                                                            if (!dataPathBySidebars.isEmpty()) {
                                                                processTabElements(driver, dataPathBySidebars, stepId, sideBarText, jsonId, "sidebar");
                                                                dataPathBySidebars.clear();
                                                            }

                                                        } else if (kpiTypeId == 16641793815766L) {
                                                            workingOnCrudFunction(driver, id, indicatorId, headerTabText, groupName, sideBarText, jsonId);
                                                        }

                                                        // finding metadata criteria
                                                        if (metaTypeId == 200101010000011L) {
                                                            boolean isCriteria = findBpSelectorFirstCriteria(driver, metaDataId, sideBarText);
                                                            List<WebElement> dataPathByCriterias;
                                                            if (isCriteria) {
                                                                dataPathByCriterias = findDataPathByCriteria(driver, metaDataId);
                                                            } else {
                                                                dataPathByCriterias = findDataPathByWithoutCriteria(driver, metaDataId);
                                                            }
                                                            if (!dataPathByCriterias.isEmpty()) {
                                                                processTabElements(driver, dataPathByCriterias, metaDataId, sideBarText, jsonId, "sidebar");
                                                                dataPathByCriterias.clear();
                                                            }

                                                            consoleLogChecker(driver, metaDataId, sideBarText, jsonId);
                                                            if (!isDuplicateLogEntry(sideBarText, metaDataId, jsonId)) {
                                                                waitUtils(driver);

                                                                if (SideBarSaveButton(driver, metaDataId)) {
                                                                    waitUtilsProduct(driver);

                                                                    Thread.sleep(1000);
                                                                    consoleLogRequiredPath(driver, metaDataId, sideBarText, jsonId);
                                                                    if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, metaDataId, headerTabText, groupName, sideBarText, "METHOD", jsonId)) {
                                                                        waitUtils(driver);

                                                                        IndicatorCustomTab customTab = createIndicatorCustomTab(id, metaDataId, headerTabText, groupName, sideBarText, "METHOD", "Алдаа гарлаа", jsonId);
                                                                        IsIndicatorMessage.addIndicatorMessage(customTab);

                                                                        LOGGER.log(Level.SEVERE, "Process failed with alert: " + id + "  stepid: " + stepId);
                                                                    }
                                                                } else {
                                                                    IndicatorCustomTab customTab = createIndicatorCustomTab(id, metaDataId, headerTabText, groupName, sideBarText, "METHOD", "Хадгалах товч олдсонгүй!", jsonId);
                                                                    IsIndicatorMessage.addIndicatorMessage(customTab);
                                                                }

                                                            } else {
                                                                IndicatorCustomTab customTab = createIndicatorCustomTab(id, metaDataId, headerTabText, groupName, sideBarText, "METHOD", "Алдаа гарлаа", jsonId);
                                                                IsIndicatorMessage.addIndicatorMessage(customTab);
                                                            }
                                                            waitUtils(driver);
                                                        } else if (metaTypeId == 200101010000016L) {

                                                            workingOnCrudFunction(driver, id, metaDataId, headerTabText, groupName, sideBarText, jsonId);

                                                        } else if (metaTypeId == 200101010000035L) {

                                                            List<WebElement> dataPathByStatement = findDataPathByStatement(driver, metaDataId);
                                                            if (!dataPathByStatement.isEmpty()) {
                                                                processTabElements(driver, dataPathByStatement, metaDataId, sideBarText, jsonId, "sidebar");
                                                                dataPathByStatement.clear();
                                                                waitUtils(driver);
                                                                clickFilterBtnByStatement(driver, metaDataId);
                                                            }

                                                            waitUtils(driver);

                                                            IsIndicatorMessage.isErrorMessagePresent(driver, id, metaDataId, headerTabText, groupName, sideBarText, "METHOD", jsonId);
                                                            waitUtils(driver);

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        sideBarGroup.clear();
                                    }
                                    waitUtils(driver);
                                    if (menuName != null) {
                                        String elementClass = sideBarGroupElement.getAttribute("class");
                                        if (elementClass.contains("nav-group-sub-mv-opened")) {
                                            menuName.click();
                                            waitUtils(driver);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    waitUtils(driver);

                }
                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "success", "Амжилттай ажилласан.", TestProcessType, jsonId, totalCount);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findAndWorkingSiderTabsTest {0}", id + e);
            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "failed", "Алдаа гарсан.", TestProcessType, jsonId, totalCount);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Лог шалгах">
    public static boolean isDuplicateLogEntry(String systemName, String id, String jsonId) {
        return ElementsFunctionUtils.ProcessLogFields.get().stream()
                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Алдааны мэдээлэл илгээх">
    public static IndicatorCustomTab createIndicatorCustomTab(String parentId, String stepId, String headerTabText, String groupName, String sideBarText, String indicatorType, String messageText, String jsonId) {
        IndicatorCallMethod.getProcessMetaDataList("https://dev.veritech.mn/restapi", "batdelger", "123", parentId, stepId, headerTabText, groupName, sideBarText, "failed", indicatorType, messageText, jsonId);
        return new IndicatorCustomTab(
                parentId, stepId, headerTabText, groupName, sideBarText, indicatorType, "failed", messageText, jsonId,
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
                        .stream().filter(detail -> detail.getMetaDataId().equals(stepId)).collect(Collectors.toList())
        );
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Хадгалах товч">
    private static boolean SideBarSaveButton(WebDriver driver, String stepid) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement mainDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[id='mv_checklist_id_" + stepid + "']")
            ));
            WebElement saveButton = mainDiv.findElement(By.cssSelector(".bp-btn-saveadd"));
            saveButton.sendKeys(" ");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CustomTabSaveButton {0}", stepid);
            return false;

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Group доторх мену олох">
    public static List<WebElement> findSubMenuItems(WebElement sideBarGroupElement) {
        try {
            return sideBarGroupElement.findElements(By.cssSelector(".mv_checklist_02_sub"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error finding submenu items: " + e.getMessage());
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menu нэр олох">
    public static WebElement findSubMenuName(WebElement sideBarGroupElement) {
        try {
            return sideBarGroupElement.findElement(By.cssSelector(".mv_checklist_02_groupname"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error finding submenu item: " + e.getMessage());
            return null;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sidebar group">
    public static List<WebElement> findSideBarByTabGroup(WebDriver driver, String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='" + tabId + "'] .nav-sidebar")));
            List<WebElement> elements = MainTab.findElements(By.cssSelector(".nav-item.nav-item-submenu"));
            return new ArrayList<>(elements);
        } catch (NoSuchElementException n) {
            LOGGER.log(Level.WARNING, "NoSuchElementException findSideBarByTab");
            return List.of();
        } catch (TimeoutException t) {
            LOGGER.log(Level.WARNING, "TimeoutException findSideBarByTab");
            return List.of();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error findSideBarByTab");
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Header Tab">
    public static List<WebElement> findHeaderTabs(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mv-checklist2-render-parent .mv-checklist-tab")));
            List<WebElement> elements = MainTab.findElements(By.cssSelector(".mv-checklist-tab-link"));
            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error findHeaderTabs");
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Таб дотор ашигласан section олох">
    public static List<WebElement> findSectionsGroupTab(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + id + "']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findSectionsGroupTab Elements 'data-action-path' not found {0}", id);
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Бүх section дээр addRow ажлуулах">
    public static void clickAddRowButtons(List<WebElement> sections) {
        for (WebElement section : sections) {
            try {
                String sectionPath = section.getAttribute("data-section-path");
                List<WebElement> addRowActions = section.findElements(By.cssSelector("div.mv-add-row-actions"));

                for (WebElement addRowAction : addRowActions) {
                    try {
                        WebElement addButton = addRowAction.findElement(By.cssSelector("button.bp-add-one-row"));
                        if (addButton.isDisplayed() && addButton.isEnabled()) {
                            addButton.click();
//                            LOGGER.log(Level.INFO, "Clicked add row button in section: {0}", sectionPath);
                        }
                    } catch (NoSuchElementException e) {
                        LOGGER.log(Level.WARNING, "No add button found in section: {0}", sectionPath);
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while clicking add row buttons", e);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SideBar доторх датапат олох">
    public static List<WebElement> findDataPathBySidebar(WebDriver driver, String stepId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + stepId + "']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Criteria-тэй checklist сонгосны дараагаар датапат олох">
    public static List<WebElement> findDataPathByCriteria(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            String uniqid = MainProcess.getAttribute("data-bp-uniq-id");
            WebElement MainTab = MainProcess.findElement(By.cssSelector("div[id='wizard-" + uniqid + "']"));

            List<WebElement> elements = MainTab.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataPath олох тайлан">
    public static List<WebElement> findDataPathByStatement(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dataview-statement-search-" + id + "']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="search btn">
    public static void clickFilterBtnByStatement(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dataview-statement-search-" + id + "']")));
            WebElement MainBtn = MainProcess.findElement(By.cssSelector("button.dataview-statement-filter-btn"));
            MainBtn.click();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error clickFilterBtnByStatement {0}", id);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Criteria байхгүй бол datapath олох">
    public static List<WebElement> findDataPathByWithoutCriteria(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Criteria байвал next дарна">
    public static boolean findBpSelectorFirstCriteria(WebDriver driver, String id, String name) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + id + "']")));

            WebElement elements = MainProcess.findElement(By.cssSelector(".actions .btn-primary"));
            if (elements.isDisplayed()) {
                elements.click();
                Thread.sleep(1000);

            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findBpSelectorFirstCriteria error: {0}", name);
            return false;
        }
    }
    //</editor-fold>

    public static List<WebElement> findIndicatorCruds(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement mainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + id + "'] .dv-process-buttons")));
            return mainProcess.findElements(By.tagName("a"));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }

    /**
     * checklist дээрээс эхний мөр олох
     */
    public static boolean clickFirstRowByList(WebDriver driver, String id, String indicatorId, String headerTabText, String groupName, String sideBarText, String indicatorText, String jsonId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            waitUtils(driver);

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
//                    "//div[contains(@id,'mv_checklist_id_"+ indicatorId +"')]//tr[contains(@id,'datagrid-row-r')]"
//            )));
            WebElement parentRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[id='mv_checklist_id_" + indicatorId + "'] .datagrid-view2 .datagrid-body table")
            ));
            String visibility = parentRow.getAttribute("visibility");
            if ("hidden".equalsIgnoreCase(visibility)) {
                IndicatorCustomTab customTab = createIndicatorCustomTab(id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, "Тохирох утга олдсонгүй!", jsonId);
                IsIndicatorMessage.addIndicatorMessage(customTab);
                return false;
            } else {
                List<WebElement> rows = parentRow.findElements(By.xpath(
                        ".//tr[contains(@id,'datagrid-row-r')]"
                ));
                if (!rows.isEmpty()) {
                    Thread.sleep(500);
                    WebElement firstRow = rows.get(0);
                    WebElement firstCell = firstRow.findElement(By.xpath(".//td[1]"));
                    if (firstCell != null) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstCell);
                        return true;
                    }
                }
            }
            IndicatorCustomTab customTab = createIndicatorCustomTab(id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, "Тохирох утга олдсонгүй!", jsonId);
            IsIndicatorMessage.addIndicatorMessage(customTab);
            throw new NoSuchElementException("First row not found");

        } catch (Exception e) {

            IndicatorCustomTab customTab = createIndicatorCustomTab(id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, "Тохирох утга олдсонгүй!", jsonId);
            IsIndicatorMessage.addIndicatorMessage(customTab);

            return false;
        }
    }

    public static void clickYesOrNoDialog(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            WebElement dialog = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div[role='dialog']")));

            if (dialog.isDisplayed() && !dialog.getAttribute("style").contains("display: none")) {
                WebElement yesButton = dialog.findElement(By.xpath(".//button[contains(text(), 'Тийм')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to click on 'Тийм' button");
        }
    }

    private static void workingOnCrudFunction(WebDriver driver, String id, String indicatorId, String headerTabText, String groupName, String sideBarText, String jsonId) {
        try {
            if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, indicatorId, headerTabText, groupName, sideBarText, "METHOD", jsonId)) {
                waitUtils(driver);

                List<WebElement> indicatorCruds = findIndicatorCruds(driver, indicatorId);
                if (!indicatorCruds.isEmpty()) {
                    for (WebElement indicatorCrud : indicatorCruds) {
                        String actionType = indicatorCrud.getAttribute("data-actiontype");
                        String indicatorText = indicatorCrud.getText();
                        if (actionType.equals("create") || actionType.equals("insert")) {
                            indicatorCrud.click();
                            waitUtils(driver);
                            IndicatorPathByProduct.isIndicatorPersent(driver, id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, jsonId);
                            waitUtils(driver);
                        } else if (actionType.equals("update") || actionType.equals("delete")) {
                            waitUtils(driver);
                            if (clickFirstRowByList(driver, id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, jsonId)) {
                                if (actionType.equals("update")) {
                                    indicatorCrud.click();
                                    waitUtils(driver);
                                    if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, jsonId)) {

                                        IndicatorPathByProduct.isIndicatorPersent(driver, id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, jsonId);
                                        waitUtils(driver);

                                    }
                                } else {
                                    waitUtils(driver);
                                    indicatorCrud.click();
                                    waitUtils(driver);
                                    if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, jsonId)) {
                                        clickYesOrNoDialog(driver);
                                        waitUtils(driver);
                                        IsIndicatorMessage.isErrorMessagePresent(driver, id, indicatorId, headerTabText, groupName, sideBarText, indicatorText, jsonId);
                                    }

                                }
                            }
                        }
                    }
                    indicatorCruds.clear();
                }

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to workingOnCrudFunction {0}", indicatorId);
        }
    }
}

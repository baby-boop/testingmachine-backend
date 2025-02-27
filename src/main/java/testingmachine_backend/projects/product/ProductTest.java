package testingmachine_backend.projects.product;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;

public class ProductTest {

    private static final int SHORT_WAIT_SECONDS = 2;

    static final Logger LOGGER = Logger.getLogger(ProductTest.class.getName());

    public static void findAndWorkingSiderTabsTest(WebDriver driver, String systemName, String id, String code, String name, String TestProcessType, String jsonId) {
        try{
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

                    if(headerTabText.equals("Бүртгэл")){

                        headerTab.click();
                        waitUtils(driver);

                        /* Бүх group олох */
                        List<WebElement> sideBarGroupElements = findSideBarByTabGroup(driver, tabId);
                        if(!sideBarGroupElements.isEmpty()) {
                            for (WebElement sideBarGroupElement : sideBarGroupElements) {

                                /* Group нэр олох клик хийх */
                                WebElement menuName = findSubMenuName(sideBarGroupElement);
                                if(menuName != null) {
                                    String elementClass = sideBarGroupElement.getAttribute("class");

                                    if (!elementClass.contains("nav-group-sub-mv-opened")) {

                                        menuName.click();
                                        waitUtils(driver);
                                    }else{
                                        System.out.println("Not clicked menuName: " + menuName.getText());
                                    }
                                }
                                String groupName = menuName.getText();
                                /* group доторх sideBar олох */
                                List<WebElement> sideBarGroup = findSubMenuItems(sideBarGroupElement);
                                if(!sideBarGroup.isEmpty()) {
                                    for (WebElement sideBarElement : sideBarGroup) {

                                        sideBarElement.click();
                                        waitUtils(driver);

                                        String sideBarText = sideBarElement.getText();
                                        String stepId = sideBarElement.getAttribute("data-stepid");
                                        String jsonData = sideBarElement.getAttribute("data-json");
//                                        consoleLogChecker(driver, stepId, sideBarText, jsonId);

                                        if(jsonData != null){
                                            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

                                            long kpiTypeId = jsonObject.has("kpiTypeId") ? jsonObject.get("kpiTypeId").getAsLong() : 0L;
                                            String metaDataId = jsonObject.has("metaDataId") && !jsonObject.get("metaDataId").isJsonNull() ? jsonObject.get("metaDataId").getAsString() : "null";
                                            long metaTypeId = jsonObject.has("metaTypeId") && !jsonObject.get("metaTypeId").isJsonNull() ? jsonObject.get("metaTypeId").getAsLong() : 0L;
                                            String typeCode = jsonObject.has("typeCode") && !jsonObject.get("typeCode").isJsonNull() ? jsonObject.get("typeCode").getAsString() : "null";

                                            if(kpiTypeId != 0L && metaDataId == null){
                                                if(kpiTypeId == 2008L){
                                                    List<WebElement> sections = findSectionsGroupTab(driver, stepId);
                                                    if(!sections.isEmpty()){
                                                        clickAddRowButtons(sections);
                                                        sections.clear();
                                                    }

                                                    List<WebElement> dataPathBySidebars = findDataPathBySidebar(driver, stepId);
                                                    processTabElements(driver, dataPathBySidebars, stepId, sideBarText, jsonId, "sidebar");
                                                    dataPathBySidebars.clear();


                                                }else if(kpiTypeId == 16641793815766L){

                                                    System.out.println("List: " + sideBarText);

                                                }
                                            }else if(kpiTypeId == 0L && metaTypeId != 0L){
                                                System.out.println("Metadata daraa ni hiinee");
                                            }
                                            else if(kpiTypeId == 1070L){

                                                // finding metadata criteria
                                                if(metaTypeId == 200101010000011L){
                                                    boolean isCriteria = findBpSelectorFirstCriteria(driver, metaDataId, sideBarText);
                                                    List<WebElement> dataPathByCriterias;
                                                    if(isCriteria){
                                                        dataPathByCriterias = findDataPathByCriteria(driver, metaDataId);
                                                    }else{
                                                        dataPathByCriterias = findDataPathByWithoutCriteria(driver, metaDataId);
                                                    }
                                                    if(!dataPathByCriterias.isEmpty()){
                                                        processTabElements(driver, dataPathByCriterias, metaDataId, sideBarText, jsonId, "sidebar");
                                                        dataPathByCriterias.clear();
                                                    }

                                                    consoleLogChecker(driver, stepId, sideBarText, jsonId);
                                                    if (!isDuplicateLogEntry(sideBarText, stepId, jsonId)) {
                                                        waitUtils(driver);
                                                        SideBarSaveButton(driver, metaDataId);
                                                        waitUtils(driver);

                                                        Thread.sleep(1000);
                                                        consoleLogRequiredPath(driver, stepId, sideBarText, jsonId);
                                                        if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, stepId, headerTabText, groupName, sideBarText, "METHOD", jsonId)) {
                                                            waitUtils(driver);

                                                            IndicatorCustomTab customTab = createIndicatorCustomTab(id, stepId, headerTabText,groupName, sideBarText, "METHOD", jsonId);
                                                            IsIndicatorMessage.addIndicatorMessage(customTab);

                                                            LOGGER.log(Level.SEVERE, "Process failed with alert: " + id + "  stepid: " + stepId);
                                                        }
                                                    }else {
                                                        IndicatorCustomTab customTab = createIndicatorCustomTab(id, stepId, headerTabText,groupName, sideBarText, "METHOD", jsonId);
                                                        IsIndicatorMessage.addIndicatorMessage(customTab);
                                                    }
                                                    waitUtils(driver);
//

                                                }
                                                //finding metadata list
                                                else if(metaTypeId == 200101010000016L && metaDataId == null){

//                                                    List<WebElement> dataPathBySidebars = findDataPathBySidebar(driver, stepId);
//                                                    processTabElements(driver, dataPathBySidebars, stepId, sideBarText, jsonId, "sidebar");
//                                                    dataPathBySidebars.clear();
                                                }else{

                                                }

                                            }
                                        }
                                    }
                                    sideBarGroup.clear();
                                }
                                waitUtils(driver);
                                if(menuName != null) {
                                    String elementClass = sideBarGroupElement.getAttribute("class");
                                    if (elementClass.contains("nav-group-sub-mv-opened")) {
                                        menuName.click();
                                        waitUtils(driver);
                                    }
                                }
                            }
                        }
                    }
                    waitUtils(driver);

                }
                ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, "success", "Амжилттай ажилласан.", TestProcessType, jsonId);
            }
        }catch (Exception e){
            LOGGER.log(Level.WARNING, "findAndWorkingSiderTabsTest {0}", e.getMessage());
        }
    }

    public static boolean isDuplicateLogEntry(String systemName, String id, String jsonId) {
        return ElementsFunctionUtils.ProcessLogFields.get().stream()
                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId));
    }

    private static IndicatorCustomTab createIndicatorCustomTab(String id, String stepId, String headerTabText, String groupName, String sideBarText, String indicatorType, String jsonId) {
        return new IndicatorCustomTab(
                id, stepId, headerTabText, groupName, sideBarText, indicatorType, "failed", "Алдаа гарлаа", jsonId,
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

    public static void SideBarSaveButton(WebDriver driver, String stepid) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='mv_checklist_id_" + stepid + "']//button[contains(@class, 'bpMainSaveButton')]")
            ));
            saveButton.click();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CustomTabSaveButton", e);
        }
    }

    public static List<WebElement> findSubMenuItems(WebElement sideBarGroupElement) {
        try {
            return sideBarGroupElement.findElements(By.cssSelector(".mv_checklist_02_sub"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error finding submenu items: " + e.getMessage());
            return List.of();
        }
    }

    public static WebElement findSubMenuName(WebElement sideBarGroupElement) {
        try {
            return sideBarGroupElement.findElement(By.cssSelector(".mv_checklist_02_groupname"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error finding submenu item: " + e.getMessage());
            return null;
        }
    }


    public static List<WebElement> findSideBarByTabGroup(WebDriver driver, String tabId ) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='" + tabId +"'] .nav-sidebar")));
            List<WebElement> elements = MainTab.findElements(By.cssSelector(".nav-item.nav-item-submenu"));
            return new ArrayList<>(elements);
        }catch (NoSuchElementException n){
            LOGGER.log(Level.WARNING, "NoSuchElementException findSideBarByTab");
            return List.of();
        }catch (TimeoutException t) {
            LOGGER.log(Level.WARNING, "TimeoutException findSideBarByTab");
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

    /* Таб дотор ашигласан section олох */
    public static List<WebElement> findSectionsGroupTab(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_"+ id +"']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findSectionsGroupTab Elements 'data-action-path' not found {0}", id);
            return List.of();
        }
    }

    /* Бүх section дээр addRow ажлуулах */
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


    /* SideBar доторх датапат олох */
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

    /* Criteria-тэй checklist сонгосны дараагаар датапат олох */
    public static List<WebElement> findDataPathByCriteria(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id +"']")));
            String uniqid = MainProcess.getAttribute("data-bp-uniq-id");
            WebElement MainTab = MainProcess.findElement(By.cssSelector("div[id='wizard-" + uniqid +"']"));

            List<WebElement> elements = MainTab.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }
    public static List<WebElement> findDataPathByWithoutCriteria(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id +"']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector not found");
            return List.of();
        }
    }


    public static boolean findBpSelectorFirstCriteria(WebDriver driver, String id, String name) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='mv_checklist_id_" + id +"']")));

            WebElement elements = MainProcess.findElement(By.cssSelector(".actions .btn-primary"));
            if (elements.isDisplayed()) {
                elements.click();
                Thread.sleep(1000);

            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findBpSelectorFirstCriteria error: {0}",  name);
            return false;
        }
    }
}

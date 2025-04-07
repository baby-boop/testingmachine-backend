package testingmachine_backend.projects.product;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.projects.indicator.CheckListChecker;
import testingmachine_backend.projects.indicator.IndicatorCustomTab;
import testingmachine_backend.projects.indicator.IsIndicatorMessage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;
import static testingmachine_backend.projects.product.ProductTest.createIndicatorCustomTab;

@Slf4j
public class IndicatorPathByProduct {

    static final Logger LOGGER = Logger.getLogger(IndicatorPathByProduct.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;

    public static void isIndicatorPersent(WebDriver driver, String parentId, String id, String headerTabText, String groupName, String sideBarText, String indicatorType, String jsonId) {
        try {
            waitUtils(driver);

            consoleLogChecker(driver, id, sideBarText, jsonId);

            findMainProcessType(driver, parentId, id, headerTabText, groupName, sideBarText, indicatorType, jsonId);
         }catch (Exception e){
            LOGGER.log(Level.SEVERE, "error isIndicatorPersent {0}", id);
        }
    }

    public static void findMainProcessType(WebDriver driver, String parentId, String id, String headerTabText, String groupName, String sideBarText, String indicatorType, String jsonId) {
        if(CheckListChecker.isCheckList(driver, id)){
            List<WebElement> sections = findSectionsGroupMain(driver, id);
            if(!sections.isEmpty()){
                clickAddRowButtons(sections);
                sections.clear();
            }

            List<WebElement> elementsWithDataPath = findElementsWithDialog(driver, id);
            if(!elementsWithDataPath.isEmpty()){
                processTabElements(driver, elementsWithDataPath, id, sideBarText, jsonId, "");
                elementsWithDataPath.clear();
            }

            waitUtils(driver);
            if(MainSaveButton(driver, id)){
                waitUtils(driver);
                if (!IsIndicatorMessage.isErrorMessagePresent(driver, parentId, id, headerTabText, groupName, sideBarText, indicatorType, jsonId)) {
                    IndicatorCustomTab customTab = createIndicatorCustomTab(parentId, id, headerTabText, groupName, sideBarText, indicatorType, "Алдаа гарлаа", jsonId);
                    IsIndicatorMessage.addIndicatorMessage(customTab);
                }
            }else {
                IndicatorCustomTab customTab = createIndicatorCustomTab(parentId, id, headerTabText, groupName, sideBarText, indicatorType, "Хадгалах товч олдсонгүй!", jsonId);
                IsIndicatorMessage.addIndicatorMessage(customTab);
            }
            clickReturnButton(driver, id);
        }else if(CheckListChecker.isBusinessProcess(driver, id)){

            WebElement dialogDiv = driver.findElement(By.cssSelector("div[id*='dialog-businessprocess-']"));
            String idValue = dialogDiv.getAttribute("id");
            String numberPart = idValue.replace("dialog-businessprocess-", "");


            List<WebElement> sections = findSectionsGroupMainBp(driver, numberPart);
            if(!sections.isEmpty()){
                clickAddRowButtons(sections);
                sections.clear();
            }

            List<WebElement> elementsWithDataPath = findElementsWithBp(driver, numberPart);
            if(!elementsWithDataPath.isEmpty()){
                processTabElements(driver, elementsWithDataPath, id, sideBarText, jsonId, "");
                elementsWithDataPath.clear();
            }

            waitUtils(driver);
            if(MainSaveButtonBp(driver, numberPart)){
                waitUtils(driver);
                if (!IsIndicatorMessage.isErrorMessagePresent(driver, parentId, id, headerTabText, groupName, sideBarText, indicatorType, jsonId)) {
                    IndicatorCustomTab customTab = createIndicatorCustomTab(parentId, id, headerTabText, groupName, sideBarText, indicatorType, "Алдаа гарлаа", jsonId);
                    IsIndicatorMessage.addIndicatorMessage(customTab);
                }
            }else{
                waitUtils(driver);
                IndicatorCustomTab customTab = createIndicatorCustomTab(parentId, id, headerTabText, groupName, sideBarText, indicatorType, "Хадгалах товч олдсонгүй!", jsonId);
                IsIndicatorMessage.addIndicatorMessage(customTab);
            }
            waitUtils(driver);
            MainCloseButtonBp(driver, numberPart);
            waitUtils(driver);
        }
    }

    //Бүх олдож буй section-олох checklist
    private static List<WebElement> findSectionsGroupMain(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-"+ id +"']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findSectionsGroup Elements 'data-action-path' not found {0}", id);
            return List.of();
        }
    }

    //CheckList үед буцах товчийг дарна
    private static void clickReturnButton(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));
            WebElement closeButton = MainProcess.findElement(
                    By.cssSelector("div a[onclick='checklistCloseDialog(this)']")
            );
            if(closeButton != null){
                closeButton.click();
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error clickReturnButton {0}", id);
        }
    }

    public static boolean MainSaveButtonBp(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-describedby='dialog-businessprocess-" + id +"']")));
            WebElement saveButton = MainProcess.findElement(By.xpath(".//button[contains(@class, 'bp-btn-save') and text()='Хадгалах']"));
            if(saveButton != null){
                saveButton.sendKeys(" ");
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error MainSaveButtonBp {0}", id);
            return false;
        }
    }

    public static void MainCloseButtonBp(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-describedby='dialog-businessprocess-" + id +"']")));
            WebElement saveButton = MainProcess.findElement(By.xpath(".//button[contains(@class, 'bp-btn-close') and text()='Хаах']"));
            if(saveButton != null){
                saveButton.sendKeys(" ");
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error MainSaveButtonBp {0}", id);
        }
    }

    public static boolean MainSaveButton(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-valuemap-" + id +"']")));
            WebElement saveButton = MainProcess.findElement(By.xpath("//div[@class='mv-checklist-main-render']//button[contains(@class, 'btn-success bp-btn-save')]"));
            if(saveButton != null){
                saveButton.sendKeys(" ");
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error MainSaveButton {0}", id);
            return false;
        }
    }

    //Section дотор add-row байвал click хийнэ checklist
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

    //Metaverse datapath олох checklist
    public static List<WebElement> findElementsWithDialog(WebDriver driver, String id) {
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


    //Бүх олдож буй section-олох bp
    public static List<WebElement> findSectionsGroupMainBp(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-"+ id +"']")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("div[data-section-path]"));

            return new ArrayList<>(elements);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "findSectionsGroupMainBp Elements 'data-action-path' not found {0}", id);
            return List.of();
        }
    }

    //Metaverse datapath олох checklist
    public static List<WebElement> findElementsWithBp(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='dialog-businessprocess-" + id +"']")));

            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));
            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "findElementsWithBp element with selector not found {0}", id);
            return List.of();
        }
    }
}

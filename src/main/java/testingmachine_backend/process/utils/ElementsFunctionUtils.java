package testingmachine_backend.process.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.process.DTO.EmptyDataDTO;
import testingmachine_backend.process.DTO.PopupStandardFieldsDTO;
import testingmachine_backend.process.DTO.ProcessLogDTO;
import testingmachine_backend.process.DTO.RequiredPathDTO;
import testingmachine_backend.process.Fields.EmptyDataField;
import testingmachine_backend.process.Fields.PopupStandartField;
import testingmachine_backend.process.Fields.ProcessLogFields;
import testingmachine_backend.process.Fields.RequiredPathField;
import testingmachine_backend.process.Messages.PopupMessage;

import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.FormFieldUtils.handleElementAction;

public class ElementsFunctionUtils {

    static final Logger LOGGER = Logger.getLogger(ElementsFunctionUtils.class.getName());

    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int LONG_WAIT_SECONDS = 90;
    private static final Pattern TAB_ID_PATTERN = Pattern.compile("tab_\\d+_\\d+");

    @EmptyDataField
    public static final List<EmptyDataDTO> emptyPathField = new ArrayList<>();

    @RequiredPathField
    public static final List<RequiredPathDTO> RequiredPathField = new ArrayList<>();

    @ProcessLogFields
    public static final List<ProcessLogDTO> ProcessLogFields = new ArrayList<>();

    @PopupStandartField
    public static final List<PopupStandardFieldsDTO> PopupStandartField = new ArrayList<>();

    public static void findTextEditorInput(WebDriver driver, String dataSPath, String id) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));

            driver.switchTo().frame("param["+ dataSPath +"]_ifr");

            WebElement textEditorField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body[data-id='param[" + dataSPath + "]']")));
            textEditorField.sendKeys("test text editor");
            driver.switchTo().defaultContent();

        }catch(Exception e){
            LOGGER.log(Level.WARNING, "Error finding text editor: " + id);
        }
    }


    public static void findElementWithPopup(WebDriver driver,WebElement element, String dataPath, String required, String id, String fileName ) {
        try{
            WebElement popupButton = element.findElement(By.xpath("..//span[@class='input-group-btn']/button"));
            if (popupButton != null) {
                popupButton.click();
                waitUtils(driver);
                clickFirstPopup(driver, id, fileName, dataPath, required);
                waitUtils(driver);
            }
        }catch(TimeoutException t){
//            System.out.println("findElementWithPopup timeout: " +id + " fileName: " + fileName + t);
        }catch (NoSuchElementException n){
            System.out.println("findElementWithPopup not found: " + id + " fileName: " + fileName + n);
        }
        catch (Exception e) {
            System.out.println("findElementWithPopup error: " + id + " fileName: " + fileName);
        }
    }

    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public static void clickFirstRow(WebDriver driver, String id, String fileName, String datapath, String required) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            waitUtils(driver);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(@id,'datagrid-row-r')]")));
            List<WebElement> rows = driver.findElements(By.xpath("//tr[contains(@id,'datagrid-row-r')]"));

            if (!rows.isEmpty()) {
                Thread.sleep(500);
                WebElement firstRow = rows.get(0);
                WebElement firstCell = firstRow.findElement(By.xpath(".//td[1]"));
                if (firstCell != null) {
                    scrollToElement(driver, firstCell);
                    rows.clear();
                    waitUtils(driver);
                    WebElement addToCartButton  = driver.findElement(By.xpath("//button[contains(@class, 'btn green-meadow btn-sm float-left')]"));
                    if(addToCartButton != null) {
                        addToCartButton.click();
                        waitUtils(driver);
                        WebElement selectButton = driver.findElement(By.xpath("//button[contains(@class, 'btn blue btn-sm datagrid-choose-btn')]"));
                        if (selectButton != null) {
                            selectButton.click();
                        }
                    }
                }
            }
            rows.clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
        } catch (TimeoutException t){
            if (PopupMessage.isErrorMessagePresent(driver, datapath, id, fileName)) {
                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                closeBtn.click();
            }else{
                if(required != null) {
                    EmptyDataDTO emptyPath = new EmptyDataDTO(fileName, id, datapath, "Popup", JsonController.getJsonId());
                    emptyPathField.add(emptyPath);
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }else{
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }
            }
        }
        catch (NoSuchElementException n){
            LOGGER.log(Level.SEVERE, "NoSuchElementException first row");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error first row");
        }
    }

    public static void comboboxFunction(WebDriver driver, String dataSPath, String required, String id, String fileName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            Thread.sleep(500);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-s-path='" + dataSPath + "']")));
            WebElement comboBoxLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-s-path='" + dataSPath + "']")));
            if (comboBoxLocator != null) {
                WebElement comboBoxes = wait.until(ExpectedConditions.elementToBeClickable(comboBoxLocator));
                comboBoxes.click();
                By comboBoxSelectLocator = By.cssSelector("select[data-path='" + dataSPath + "']");
                Thread.sleep(500);
                selectSecondOption(driver, comboBoxSelectLocator, id, dataSPath, required, fileName);
            }
        }
        catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error selecting the first visible comboBox option: " + dataSPath);
        }
    }
    public static void comboGridFunction(WebDriver driver, WebElement element, String dataPath, String id, String fileName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

            Thread.sleep(500);
            WebElement comboGridInput = element.findElement(By.xpath("..//input[contains(@onclick, 'dataViewSelectableComboGrid')]"));
            comboGridInput.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(@id,'datagrid-row-r')]")));

            List<WebElement> rows = driver.findElements(By.xpath("//tr[contains(@id,'datagrid-row-r')]"));

            if (!rows.isEmpty()) {
                WebElement firstRow = rows.get(0);

                Actions actions = new Actions(driver);
                actions.doubleClick(firstRow).perform();
            } else {
                System.out.println("No rows found in the data grid.");
            }

        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error comboGrid: " + dataPath + id + fileName);
        }
    }
    public static void iconFirstField( WebElement element, String dataPath, String id, String fileName) {
        try {
            Thread.sleep(500);
            WebElement firstListItem = element.findElement(By.xpath("..//li[1]"));
            firstListItem.click();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error comboGrid: " + dataPath + id + fileName);
        }
    }
    public static void selectSecondOption(WebDriver driver, By selectorLocator, String id, String dataPath, String required, String fileName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement selector = wait.until(ExpectedConditions.visibilityOfElementLocated(selectorLocator));
        List<WebElement> options = selector.findElements(By.tagName("option"));
        if (options.size() > 1) {
            options.get(1).click();
        } else {
            if (required != null){
                EmptyDataDTO emptyPath = new EmptyDataDTO(fileName, id, dataPath, "Combo", JsonController.getJsonId());
                emptyPathField.add(emptyPath);
//                JsonFileReader.saveToSingleJsonFile(emptyPath);
            }
            selector.sendKeys(Keys.ENTER);
        }
        selector.clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
    }
    public static Optional<String> extractTabIdentifier(String href) {
        Matcher matcher = TAB_ID_PATTERN.matcher(href);
        return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
    }
    public static Map<String, WebElement> getUniqueTabElements(List<WebElement> elements) {
        Map<String, WebElement> uniqueTabElements = new LinkedHashMap<>();
        for (WebElement element : elements) {
            String elementClass = element.getAttribute("class");
            String elementType = element.getAttribute("type");
            String dataPath = element.getAttribute("data-path");

            if(elementClass.contains("text_editorInit") ){
                if (!uniqueTabElements.containsKey(dataPath)) {
                    uniqueTabElements.put(dataPath, element);
                }
            }else
                if(elementClass.contains("popupInit") ){
                if (!uniqueTabElements.containsKey(dataPath)) {
                    uniqueTabElements.put(dataPath, element);
                }
            }
            else if (elementClass.contains("dropdownInput") || elementClass.contains("radioInit")
                    || elementType.contains("checkbox") || elementClass.contains("booleanInit")
                    || elementClass.contains("fileInit")
//                        || elementClass.contains("combogridInit")
                    || elementClass.contains("iconInit")) {
                if(!element.getAttribute("style").contains("display: none;")){
                    if (!uniqueTabElements.containsKey(dataPath)) {
                        uniqueTabElements.put(dataPath, element);
                    }
                }
            } else if (element.isDisplayed()) {
                if (!uniqueTabElements.containsKey(dataPath)) {
                    uniqueTabElements.put(dataPath, element);
                }
            }
        }
        return uniqueTabElements;
    }

    public static Map<String, String> getElementAttributes(WebElement element) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("class", element.getAttribute("class"));
        attributes.put("value", element.getAttribute("value"));
        attributes.put("type", element.getAttribute("type"));
        attributes.put("data-path", element.getAttribute("data-path"));
        attributes.put("data-regex", element.getAttribute("data-regex"));
        attributes.put("required", element.getAttribute("required"));
        return attributes;
    }

    public static void processTabElements(WebDriver driver, List<WebElement> elements, String id, String fileName) {
        if (elements != null) {
            for (WebElement element : elements) {
                Map<String, String> attributes = getElementAttributes(element);
                handleElementAction(
                        driver,
                        element,
                        attributes.get("class"),
                        attributes.get("value"),
                        attributes.get("type"),
                        attributes.get("data-path"),
                        attributes.get("data-regex"),
                        attributes.get("required"),
                        id,
                        fileName
                );
            }
        }
    }

    public static void consoleLogChecker(WebDriver driver, String id, String fileName) {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logs) {
            if (entry.getLevel() == Level.SEVERE && entry.getMessage() != null && !isIgnorableError(entry.getMessage())) {

                String logMessage = entry.getMessage();
                String uncaughtMessage ;
                if (logMessage.contains("Uncaught")) {
                    uncaughtMessage = logMessage.substring(logMessage.indexOf("Uncaught"));
                } else {
                    uncaughtMessage = logMessage;
                }

                String formattedTimestamp = new Date(entry.getTimestamp()).toString();
                LOGGER.log(Level.SEVERE, formattedTimestamp + " " + entry.getLevel() + " " + uncaughtMessage  + " " + id);
                ProcessLogDTO processLogFields = new ProcessLogDTO(fileName, id, "error", uncaughtMessage, JsonController.getJsonId());
                ProcessLogFields.add(processLogFields);

            }
        }
    }
    public static List<ProcessLogDTO> getProcessLogMessages() {
        return new ArrayList<>(ProcessLogFields);
    }
    public static void consoleLogRequiredPath(WebDriver driver, String id, String fileName) {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logs) {
            if ( entry.getMessage() != null ) {
                String logMessage = entry.getMessage();
                if(logMessage.contains("Path:")){
                    String pathMessage = logMessage.substring(logMessage.indexOf("Path:"));
                    String formattedTimestamp = new Date(entry.getTimestamp()).toString();
                    LOGGER.log(Level.INFO, formattedTimestamp + " Extracted Console Log: " + pathMessage + " " + id);

                    RequiredPathDTO requiredPaths = new RequiredPathDTO(fileName, id, "required", pathMessage, JsonController.getJsonId());
                    RequiredPathField.add(requiredPaths);
                }else if (logMessage.contains("bpResult:")){
                    String pathMessage = logMessage.substring(logMessage.indexOf("bpResult:"));
                    LOGGER.log(Level.INFO, pathMessage + " Extracted Console Log: " + id);
                }
            }
        }
    }

    public static List<RequiredPathDTO> getRequiredPathMessages() {
        return new ArrayList<>(RequiredPathField);
    }



    public static boolean isIgnorableError(String message) {
        return message.contains("Uncaught TypeError: Cannot read properties of null")
                || message.contains("Uncaught TypeError: Cannot read properties of undefined")
                || message.contains("Failed to load resource: the server responded with a status of 404 (Not Found)")
                || message.contains("Failed to load resource: the server responded with a status of 405 (Not Allowed)")
                || message.contains("Failed to load resource: the server responded with a status of 500")
                || message.contains("Failed to load resource: the server responded with a status of");
    }

    public static List<EmptyDataDTO> getUniqueEmptyDataPath() {
        Set<EmptyDataDTO> uniqueData = new LinkedHashSet<>(emptyPathField);
        return new ArrayList<>(uniqueData);
    }


    public static void clickFirstPopup(WebDriver driver, String id, String fileName, String datapath, String required) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            waitUtils(driver);

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(@id,'datagrid-row-r')]")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//tr[contains(@id,'datagrid-row-r')]")));
            List<WebElement> rows = driver.findElements(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//tr[contains(@id,'datagrid-row-r')]"));

            if (!rows.isEmpty()) {
                Thread.sleep(500);
                WebElement firstRow = rows.get(0);
                WebElement firstCell = firstRow.findElement(By.xpath(".//td[1]"));
                if (firstCell != null) {
                    scrollToElement(driver, firstCell);
                    rows.clear();
                    waitUtils(driver);
                    WebElement addToCartButton  = driver.findElement(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn green-meadow btn-sm float-left')]"));
                    if(addToCartButton != null) {
                        addToCartButton.click();
                        waitUtils(driver);
                        WebElement selectButton = driver.findElement(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue btn-sm datagrid-choose-btn')]"));
                        if (selectButton != null) {
                            selectButton.click();
                        }
                    }
                }

                if(required != null) {
                    Thread.sleep(2000);
                    findNameAndCodeWithPopup(driver, datapath, id, fileName);
                }
            }
            rows.clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
        } catch (TimeoutException t){
            if (PopupMessage.isErrorMessagePresent(driver, datapath, id, fileName)) {
                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                closeBtn.click();
            }else{
                if(required != null) {
                    EmptyDataDTO emptyPath = new EmptyDataDTO(fileName, id, datapath, "Popup", JsonController.getJsonId());
                    emptyPathField.add(emptyPath);
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }else{
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }
            }
        }
        catch (NoSuchElementException n){
            LOGGER.log(Level.SEVERE, "NoSuchElementException first row");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error first row");
        }
    }


    public static void findNameAndCodeWithPopup(WebDriver driver, String dataPath, String id, String fileName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            waitUtils(driver);
            WebElement dataPathField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-path='" + dataPath + "']")));
            WebElement nameField = dataPathField.findElement(By.xpath(".//input[contains(@class, 'lookup-code-autocomplete')]"));
            if (nameField != null) {

                String titleValue = nameField.getAttribute("title");
                if (titleValue != null && !titleValue.isEmpty()) {
                    System.out.println("Title has a value: " + titleValue);
                } else {
                    System.out.println("Title is empty or null");
                    PopupStandardFieldsDTO popupStandardFields = new PopupStandardFieldsDTO(fileName, id, dataPath, "code", JsonController.getJsonId());
                    PopupStandartField.add(popupStandardFields);
//                    JsonFileReader.saveToSingleJsonFile(popupStandardFields);
                }


            } else {
                System.out.println("Element not found.");
            }
        }catch (TimeoutException t){
            LOGGER.log(Level.WARNING, "findNameAndCodeWithPopup TimeoutException: " + id);
        }
        catch (NoSuchElementException n){
            LOGGER.log(Level.WARNING, "findNameAndCodeWithPopup NoSuchElementException: " + id);
        }
        catch (Exception e){
            LOGGER.log(Level.WARNING, "Error finding name and code with findNameAndCodeWithPopup: " + id);
        }
    }
    public static List<PopupStandardFieldsDTO> getPopupStandartMessages() {
        return new ArrayList<>(PopupStandartField);
    }
}

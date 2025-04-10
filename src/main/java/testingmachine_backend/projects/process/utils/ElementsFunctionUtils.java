package testingmachine_backend.projects.process.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v128.network.Network;
import org.openqa.selenium.devtools.v128.network.model.RequestId;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.projects.process.DTO.*;
import testingmachine_backend.projects.process.Fields.*;
import testingmachine_backend.projects.process.Messages.PopupMessage;

import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.projects.process.utils.FormFieldUtils.handleElementAction;

public class ElementsFunctionUtils {

    static final Logger LOGGER = Logger.getLogger(ElementsFunctionUtils.class.getName());

    private static final int SHORT_WAIT_SECONDS = 4;
    private static final int LONG_WAIT_SECONDS = 90;
    private static final Pattern TAB_ID_PATTERN = Pattern.compile("tab_\\d+_\\d+");

    @EmptyDataField
    public static final ThreadLocal<List<EmptyDataDTO>> emptyPathField = ThreadLocal.withInitial(ArrayList::new);

    @testingmachine_backend.projects.process.Fields.RequiredPathField
    public static final ThreadLocal<List<RequiredPathDTO>> RequiredPathField = ThreadLocal.withInitial(ArrayList::new);

    @testingmachine_backend.projects.process.Fields.ProcessLogFields
    public static final ThreadLocal<List<ProcessLogDTO>> ProcessLogFields = ThreadLocal.withInitial(ArrayList::new);

    @testingmachine_backend.projects.process.Fields.PopupStandartField
    public static final ThreadLocal<List<PopupStandardFieldsDTO>> PopupStandartField = ThreadLocal.withInitial(ArrayList::new);

    @testingmachine_backend.projects.process.Fields.ComboMessageField
    public static final ThreadLocal<List<ComboMessageDTO>> ComboMessageField = ThreadLocal.withInitial(ArrayList::new);

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


    public static void findElementWithPopup(WebDriver driver,WebElement element, String dataPath, String required, String id, String fileName, String jsonId) {
        try{
            WebElement popupButton = element.findElement(By.xpath("..//span[@class='input-group-btn']/button"));
            if (popupButton != null) {
                popupButton.click();
                waitUtils(driver);
                clickFirstPopup(driver, id, fileName, dataPath, required, jsonId);
                waitUtils(driver);
            }
        }catch(TimeoutException t){
//            System.out.println("findElementWithPopup timeout: " +id + " fileName: " + fileName + t);
        }catch (NoSuchElementException n){
//            System.out.println("findElementWithPopup not found: " + id + " fileName: " + fileName + n);
        }
        catch (Exception e) {
//            System.out.println("findElementWithPopup error: " + id + " fileName: " + fileName);
        }
    }

    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }
//lOG
    public static void clickFirstRow(WebDriver driver, String id, String fileName, String datapath, String required, String jsonId) {
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
            if (PopupMessage.isErrorMessagePresent(driver, datapath, id, fileName, jsonId)) {
                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                closeBtn.click();
            }else{
                if(required != null) {
                    EmptyDataDTO emptyPath = new EmptyDataDTO(fileName, id, datapath, "Popup", jsonId);
                    emptyPathField.get().add(emptyPath);
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }else{
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }
            }
        }
        catch (NoSuchElementException n){
            LOGGER.log(Level.SEVERE, "NoSuchElementException first row 1");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error first row 1");
        }
    }

    public static void comboboxFunction(WebDriver driver, String dataSPath, String required, String id, String fileName, String jsonId, String indicatorType) {

        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.clearBrowserCache());
        devTools.send(Network.setCacheDisabled(true));

        AtomicReference<String> responseBody = new AtomicReference<>("");

        devTools.addListener(Network.responseReceived(), responseReceived -> {
            RequestId requestId = responseReceived.getRequestId();
            if(responseReceived.getResponse().getUrl().contains("comboDataSet")) {
                String body = devTools.send(Network.getResponseBody(requestId)).getBody();
                responseBody.set(body);
            }
        });

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

            WebElement comboBoxLocator;

            if (indicatorType.equals("sidebar")) {
                By sidebarLocator = By.cssSelector("div[id='mv_checklist_id_" + id + "'] div[data-s-path='" + dataSPath + "']");
                wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarLocator));
                comboBoxLocator = driver.findElement(sidebarLocator);
            } else if (indicatorType.equals("indicator")) {
                By sidebarLocator = By.cssSelector("div[id='dialog-valuemap-" + id + "'] div[data-s-path='" + dataSPath + "']");
                wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarLocator));
                comboBoxLocator = driver.findElement(sidebarLocator);
            }else {
                By genericLocator = By.cssSelector("div[data-s-path='" + dataSPath + "']");
                wait.until(ExpectedConditions.visibilityOfElementLocated(genericLocator));
                comboBoxLocator = driver.findElement(genericLocator);
            }

            if (comboBoxLocator != null) {
                WebElement comboBoxElement = wait.until(ExpectedConditions.elementToBeClickable(comboBoxLocator));
                comboBoxElement.click();

                By comboBoxSelectLocator;

                if(indicatorType.equals("sidebar")) {
                    comboBoxSelectLocator = By.cssSelector("div[id='mv_checklist_id_"+ id +"'] select[data-path='" + dataSPath + "']");
                    wait.until(ExpectedConditions.elementToBeClickable(comboBoxSelectLocator));
                }else if(indicatorType.equals("indicator")) {
                    comboBoxSelectLocator = By.cssSelector("div[id='dialog-valuemap-"+ id +"'] select[data-path='" + dataSPath + "']");
                }
                else{
                    comboBoxSelectLocator = By.cssSelector("select[data-path='" + dataSPath + "']");
                }

                wait.until(ExpectedConditions.elementToBeClickable(comboBoxSelectLocator));

                if (responseBody.get().contains("errorMessage")) {
                    JsonObject jsonResponse = JsonParser.parseString(responseBody.get()).getAsJsonObject();
                    String errorMessage = jsonResponse.has("errorMessage") ? jsonResponse.get("errorMessage").getAsString() : null;
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                        if (!isDuplicateNetwork(id, dataSPath, jsonId)) {
                            System.out.println("Found combox errorMessage id: " + id + " dataPath: " + dataSPath );
                            ComboMessageDTO comboMessageDTO = new ComboMessageDTO(fileName, id, dataSPath, jsonId, errorMessage);
                            ComboMessageField.get().add(comboMessageDTO);
                        } else {
                            System.out.println("Duplicated data: " + "id: " + id + " dataPath: " + dataSPath + " responseBody: " + responseBody.get());
                        }
                    }
                }

                selectSecondOption(driver, comboBoxSelectLocator, id, dataSPath, required, fileName, jsonId);

            }
        } catch (Exception e) {
//            System.err.println("Error with comboBox: " + e.getMessage());
        } finally {

        }
    }

    public static String getNetworkResponse(BrowserMobProxy proxy, String requestFilter) {
        if (proxy == null) return "";

        return proxy.getHar().getLog().getEntries().stream()
                .filter(entry -> entry.getRequest().getUrl().contains(requestFilter))
                .map(entry -> entry.getResponse().getContent().getText()) // Extract the response body
                .findFirst()
                .orElse("");
    }
    public static boolean isDuplicateNetwork(String id, String dataPath, String jsonId) {
        return ElementsFunctionUtils.ComboMessageField.get().stream()
                .anyMatch(log -> log.getMetaDataId().equals(id) && log.getDataPath().equals(dataPath) && log.getJsonId().equals(jsonId) );
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
    public static void selectSecondOption(WebDriver driver, By selectorLocator, String id, String dataPath, String required, String fileName, String jsonId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement selector = wait.until(ExpectedConditions.visibilityOfElementLocated(selectorLocator));

            List<WebElement> options = selector.findElements(By.tagName("option"));
            if (options.size() > 1) {
                options.get(1).click();

            } else {

                if (required != null){
                    LOGGER.log(Level.SEVERE, "Required option with combo not found.");
                    EmptyDataDTO emptyPath = new EmptyDataDTO(fileName, id, dataPath, "Combo", jsonId);
                    emptyPathField.get().add(emptyPath);
                }
                selector.sendKeys(Keys.ENTER);
            }

            options.clear();
            selector.clear();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
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
            if ( elementClass.isEmpty() && elementType==null) {
                continue;
            }
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
                    || elementClass.contains("fileInit") || elementClass.contains("combogridInit")
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

    public static void processTabElements(WebDriver driver, List<WebElement> elements, String id, String fileName, String jsonId, String indicatorType) {
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
                        fileName,
                        jsonId,
                        indicatorType
                );
            }
        }
    }

    public static void consoleLogChecker(WebDriver driver, String id, String fileName, String jsonId) {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logs) {
            if (/*entry.getLevel() == Level.SEVERE &&*/ entry.getMessage() != null && !isIgnorableError(entry.getMessage())) {

                String logMessage = entry.getMessage();
                String uncaughtMessage;
                if (logMessage.contains("Uncaught")) {
                    uncaughtMessage = logMessage.substring(logMessage.indexOf("Uncaught"));
                    if(!isDuplicateLogWrite( fileName, id, jsonId, uncaughtMessage)){
                        String formattedTimestamp = new Date(entry.getTimestamp()).toString();
                        LOGGER.log(Level.SEVERE, formattedTimestamp + " " + entry.getLevel() + " " + uncaughtMessage  + " " + id + "  " + jsonId);
                        ProcessLogDTO processLogFields = new ProcessLogDTO(fileName, id, "error", uncaughtMessage, jsonId);
                        ProcessLogFields.get().add(processLogFields);
                    }
                } else if (logMessage.contains("#expressionMessage:")) {
                    String modifiedMessage = logMessage.substring(logMessage.indexOf("expressionMessage") + "expressionMessage:".length());
                    uncaughtMessage = modifiedMessage.replaceAll("\\s*#expressionMessage:\\s*|_\\d+", " ");
                    String formattedTimestamp = new Date(entry.getTimestamp()).toString();
                    LOGGER.log(Level.SEVERE, formattedTimestamp + " " + entry.getLevel() + " " + uncaughtMessage  + " " + id);
                    ProcessLogDTO processLogFields = new ProcessLogDTO(fileName, id, "error", uncaughtMessage, jsonId);
                    ProcessLogFields.get().add(processLogFields);
                }
            }
        }
        driver.manage().logs().get(LogType.BROWSER);
    }

    public static boolean isDuplicateLogWrite(String systemName, String id, String jsonId, String uncaughtMessage) {
        return ElementsFunctionUtils.ProcessLogFields.get().stream()
                .anyMatch(log -> log.getModuleName().equals(systemName) && log.getMetaDataId().equals(id) && log.getJsonId().equals(jsonId) && log.getMessageText().equals(uncaughtMessage));
    }
    public static List<ProcessLogDTO> getProcessLogMessages() {
        return new ArrayList<>(ProcessLogFields.get());
    }
    public static void consoleLogRequiredPath(WebDriver driver, String id, String fileName, String jsonId) {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logs) {
            if ( entry.getMessage() != null ) {
                String logMessage = entry.getMessage();
                if(logMessage.contains("Path:")){
                    String pathMessage = logMessage.substring(logMessage.indexOf("Path:"));
                    String splitPath = pathMessage.replace("\"", "");
                    String formattedTimestamp = new Date(entry.getTimestamp()).toString();
                    LOGGER.log(Level.INFO, formattedTimestamp + " Extracted Console Log: " + splitPath + " " + id);

                    RequiredPathDTO requiredPaths = new RequiredPathDTO(fileName, id, "required", splitPath, jsonId);
                    RequiredPathField.get().add(requiredPaths);
                }else if (logMessage.contains("bpResult:")){
                    String pathMessage = logMessage.substring(logMessage.indexOf("bpResult:"));
                    LOGGER.log(Level.INFO, pathMessage + " Extracted Console Log: " + id);
                }
            }
        }
        driver.manage().logs().get(LogType.BROWSER);
    }

    public static List<RequiredPathDTO> getRequiredPathMessages() {
        return new ArrayList<>(RequiredPathField.get());
    }

    public static List<ComboMessageDTO> getComboMessages() {
        return new ArrayList<>(ComboMessageField.get());
    }

    public static boolean isIgnorableError(String message) {
        return message.contains("Uncaught TypeError: Cannot read properties of null")
                || message.contains("Uncaught TypeError: Cannot read properties of undefined")
                || message.contains("Uncaught ReferenceError: showdeed_")
                || message.contains("Failed to load resource: the server responded with a status of 404 (Not Found)")
                || message.contains("Failed to load resource: the server responded with a status of 405 (Not Allowed)")
                || message.contains("Failed to load resource: the server responded with a status of 500")
                || message.contains("Failed to load resource: the server responded with a status of");
    }

    public static List<EmptyDataDTO> getUniqueEmptyDataPath() {
        Set<EmptyDataDTO> uniqueData = new LinkedHashSet<>(emptyPathField.get());
        return new ArrayList<>(uniqueData);
    }


    public static void clickFirstPopup(WebDriver driver, String id, String fileName, String datapath, String required, String jsonId) {
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
                    findNameAndCodeWithPopup(driver, datapath, id, fileName, jsonId);
                }
            }
            rows.clear();
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
        } catch (TimeoutException t){
            if (PopupMessage.isErrorMessagePresent(driver, datapath, id, fileName, jsonId)) {
                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                closeBtn.click();
            }else{
                if(required != null) {
                    EmptyDataDTO emptyPath = new EmptyDataDTO(fileName, id, datapath, "Popup", jsonId);
                    emptyPathField.get().add(emptyPath);
                    System.out.println(emptyPath);
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }else{
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-describedby, 'dialog-dataview-selectable-')]//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }
            }
        }
        catch (NoSuchElementException n){
            LOGGER.log(Level.WARNING, "NoSuchElementException first row 2: " + datapath);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error first row 2: " + datapath);
        }
    }


    public static void findNameAndCodeWithPopup(WebDriver driver, String dataPath, String id, String fileName, String jsonId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            waitUtils(driver);
            WebElement dataPathField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-path='" + dataPath + "']")));
            WebElement nameField = dataPathField.findElement(By.xpath(".//input[contains(@class, 'lookup-code-autocomplete')]"));
            if (nameField != null) {

                String titleValue = nameField.getAttribute("title");
                if (titleValue == null && titleValue.isEmpty()) {

                    PopupStandardFieldsDTO popupStandardFields = new PopupStandardFieldsDTO(fileName, id, dataPath, "code", jsonId);
                    PopupStandartField.get().add(popupStandardFields);
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
        return new ArrayList<>(PopupStandartField.get());
    }
}

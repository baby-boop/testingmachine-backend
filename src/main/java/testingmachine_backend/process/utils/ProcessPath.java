package testingmachine_backend.process.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.support.ui.*;
import testingmachine_backend.process.DTO.ProcessLogDTO;
import testingmachine_backend.process.DTO.FailedProcessDTO;
import testingmachine_backend.process.Fields.FailedMessageField;
import testingmachine_backend.process.Fields.ProcessLogFields;
import testingmachine_backend.process.Checkers.LayoutChecker;
import testingmachine_backend.process.Section.LayoutProcessSection;
import testingmachine_backend.process.Checkers.ProcessWizardChecker;
import testingmachine_backend.process.Section.ProcessWizardSection;

import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.DetailsFieldUtils.detailActionButton;
import static testingmachine_backend.process.utils.FormFieldUtils.*;
import static testingmachine_backend.process.utils.TabDetailsFieldUtils.tabDetailItems;

@Slf4j
public class ProcessPath {
    static final Logger LOGGER = Logger.getLogger(ProcessPath.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int LONG_WAIT_SECONDS = 90;
    private static final Pattern TAB_ID_PATTERN = Pattern.compile("tab_\\d+_\\d+");
    @Getter
    private static final int failedCount = 0;

    @FailedMessageField
    private static final List<FailedProcessDTO> FailedMessageField = new ArrayList<>();

    @ProcessLogFields
    private static final List<ProcessLogDTO> ProcessLogFields = new ArrayList<>();

    public static void isProcessPersent(WebDriver driver, String id, String fileName) {
        try {

            boolean hasSevereError = checkLogsAfterAction(driver, id, fileName);

            if (!hasSevereError) {
                waitUtils(driver);

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

                saveButtonFunction(driver, id);
                waitUtils(driver);
                if (IsProcessMessage.isErrorMessagePresent(driver, id, fileName)) {
                    LOGGER.log(Level.INFO, "Process success: " + id);
                } else {
                    LOGGER.log(Level.SEVERE, "Process failed: " + id);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error process: " + id + e);
        }
    }

    public static boolean checkLogsAfterAction(WebDriver driver, String id, String fileName) {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        boolean hasSevereError = false;

        for (LogEntry entry : logs) {
            if (entry.getLevel() == Level.SEVERE && entry.getMessage() != null && !isIgnorableError(entry.getMessage())) {
                String formattedTimestamp = new Date(entry.getTimestamp()).toString();
                LOGGER.log(Level.SEVERE, formattedTimestamp + " " + entry.getLevel() + " " + entry.getMessage() + " " + id);
                hasSevereError = true;
                ProcessLogDTO processLogFields = new ProcessLogDTO(fileName, id, entry.getMessage());
                ProcessLogFields.add(processLogFields);
            }
        }
        return hasSevereError;
    }

    public static boolean isIgnorableError(String message) {
        return message.contains("Uncaught TypeError: Cannot read properties of null")
                || message.contains("Failed to load resource: the server responded with a status of 404 (Not Found)");
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

    public static WebElement findPopupButtonForElement(WebElement element) {
        try {
            Thread.sleep(1000);
            return element.findElement(By.xpath("./following-sibling::span[@class='input-group-btn']/button"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding popup button: " + e.getMessage());
            return null;
        }
    }

    private static void saveButtonFunction(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));
        try {
            WebElement wfmDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            WebElement wfmSaveButton = wfmDialog.findElement(By.xpath(".//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]"));
            wfmSaveButton.click();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Save button not found");
        }
    }

    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public static void clickFirstRow(WebDriver driver, String id, String fileName, String datapath) {
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
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
            closeBtn.click();
            FailedProcessDTO failedMessageField = new FailedProcessDTO(fileName, id, datapath);
            FailedMessageField.add(failedMessageField);
            System.out.println("fileName: " + fileName + " id: "+ id + " dataPath: "+ datapath);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error first row");
        }
    }

    static boolean isElementPresent(WebDriver driver, By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    public static void comboboxFunction(WebDriver driver, String dataSPath, String id) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            Thread.sleep(500);
            By comboBoxDivLocator = By.cssSelector("div[data-s-path='" + dataSPath + "']");
            By comboBoxSelectLocator = By.cssSelector("select[data-path='" + dataSPath + "']");

            if (isElementPresent(driver, comboBoxDivLocator)) {
                WebElement comboBoxes = wait.until(ExpectedConditions.elementToBeClickable(comboBoxDivLocator));
                comboBoxes.click();
                Thread.sleep(500);
                selectSecondOption(driver, comboBoxSelectLocator, id);
            }
        }catch (TimeoutException t){
            LOGGER.log(Level.SEVERE, "Error finding combo box: " + id);
        }
        catch (NoSuchElementException n){
            LOGGER.log(Level.SEVERE, "Error finding noelement box: " + id);
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error selecting the first visible combo box option: " + dataSPath);
        }
    }

    public static void selectSecondOption(WebDriver driver, By selectorLocator, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement selector = wait.until(ExpectedConditions.visibilityOfElementLocated(selectorLocator));
        List<WebElement> options = selector.findElements(By.tagName("option"));
        if (options.size() > 1) {
            options.get(1).click();
        } else {
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
            if (elementClass.contains("dropdownInput") || elementClass.contains("radioInit")
                    || elementType.contains("checkbox") || elementClass.contains("booleanInit") || elementClass.contains("popupInit")
                    || elementClass.contains("text_editorInit") || elementClass.contains("fileInit")) {
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
                        id,
                        fileName
                );
            }
        }
    }

    public static List<ProcessLogDTO> getProcessLogMessages() {
        return new ArrayList<>(ProcessLogFields);
    }

    public static List<FailedProcessDTO> getProcessFailed() {
        return new ArrayList<>(FailedMessageField);
    }
}







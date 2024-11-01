package testingmachine_backend.process.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.support.ui.*;
import testingmachine_backend.process.DTO.ProcessLogDTO;
import testingmachine_backend.process.Fields.ProcessLogFields;
import testingmachine_backend.process.Section.LayoutChecker;
import testingmachine_backend.process.Section.LayoutProcessSection;
import testingmachine_backend.process.Section.ProcessWizardChecker;
import testingmachine_backend.process.Section.ProcessWizardSection;

import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.DetailsFieldUtils.*;
import static testingmachine_backend.process.utils.FormFieldUtils.*;
import static testingmachine_backend.process.utils.TabDetailsFieldUtils.*;

@Slf4j
public class ProcessPath {
    static final Logger LOGGER = Logger.getLogger(ProcessPath.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int LONG_WAIT_SECONDS = 90;
    private static final Pattern TAB_ID_PATTERN = Pattern.compile("tab_\\d+_\\d+");

    @ProcessLogFields
    private static final List<ProcessLogDTO> ProcessLogFields = new ArrayList<>();

    public static void isProcessPersent(WebDriver driver, String id, String fileName) {
        try {

            LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
            boolean hasSevereError = false;

            for (LogEntry entry : logs) {

                if (entry.getLevel().toString().equals("SEVERE")) {
                    if(!entry.getMessage().contains("Uncaught TypeError: Cannot read properties of null (reading 'addClass')") && !entry.getMessage().contains("Uncaught TypeError: Cannot read properties of null (reading 'hasClass')")
                    && !entry.getMessage().contains("Failed to load resource: the server responded with a status of 404 (Not Found)") && !entry.getMessage().contains("Uncaught TypeError: Cannot read properties of undefined (reading 'id')")
                    && !entry.getMessage().contains("Uncaught TypeError: Cannot read properties of undefined (reading 'options')") && !entry.getMessage().contains("Uncaught TypeError: Cannot read properties of undefined (reading 'panel')"))
                    {
                        LOGGER.log(Level.SEVERE, new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage() + " " + id);
                        hasSevereError = true;
                        ProcessLogDTO processLogFields = new ProcessLogDTO(fileName, id , entry.getMessage());
                        ProcessLogFields.add(processLogFields);
                        break;
                    }
                }
            }

            if(!hasSevereError) {

                WaitElement.retryWaitForLoadToDisappear(driver, 3);
                WaitElement.retryWaitForLoadingToDisappear(driver, 3);

                if (LayoutChecker.isLayout(driver, id)) {
                    LayoutProcessSection.LayoutFieldFunction(driver, id);
//                    WebElement wfmDialog = waitForElementVisible(driver, By.cssSelector("div[id='bp-window-" + id + "']"), 10);
//                    WebElement wfmSaveButton = wfmDialog.findElement(By.xpath(".//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]"));
//                    wfmSaveButton.click();
                } else if (ProcessWizardChecker.isWizard(driver, id)){
                    ProcessWizardSection.KpiWizardFunction(driver, id);
                }else {
                    List<WebElement> elementsWithDataPath = findElementsWithSelector(driver, id );
                    processTabElements(driver, elementsWithDataPath, id);
                    tabDetailItems(driver, id);
                    detailActionButton(driver, id);
//                    WebElement wfmDialog = waitForElementVisible(driver, By.cssSelector("div[id='bp-window-" + id + "']"), 10);
//                    WebElement wfmSaveButton = wfmDialog.findElement(By.xpath(".//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]"));
//                    wfmSaveButton.click();
                }

                waitUtils(driver);
                if(trashMessage.isErrorMessagePresent(driver, id, fileName)){
                    log.info("Count log: " + id);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error process: " + id);
        }
    }
    public static List<WebElement> findElementsWithSelector(WebDriver driver,String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "'] .table-scrollable-borderless .bp-header-param")));
            List<WebElement> elements = MainProcess.findElements(By.cssSelector("[data-path]"));

            Map<String, WebElement> uniqueDataPathElements = new LinkedHashMap<>();
            for (WebElement element : elements) {
                String dataPath = element.getAttribute("data-path");

                if (!uniqueDataPathElements.containsKey(dataPath)) {
                    uniqueDataPathElements.put(dataPath, element);

                }
            }
            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector  not found");
            return List.of();
        }
    }

    static void findTextEditorInput(WebDriver driver, String dataSPath, String id) {
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

    static WebElement findPopupButtonForElement(WebElement element) {
        try {

            WebElement parentField = element.findElement(By.xpath("./following-sibling::span[@class='input-group-btn']/button"));
            return parentField;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Popup button not found ");
            return null;
        }
    }
    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    public static void clickFirstRow(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));
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
                    Actions actions = new Actions(driver);
                    actions.moveToElement(firstCell).click().perform();
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

                } else {
                    LOGGER.log(Level.INFO, "First cell in the first row not found: " + id);
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }
            }
            rows.clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error first row");
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
            closeBtn.click();
        }
    }

    static boolean isElementPresent(WebDriver driver, By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    static void selectComboSecondOption(WebDriver driver, String dataSPath, String id) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            By comboBoxDivLocator = By.cssSelector("div[data-s-path='" + dataSPath + "']");
            By comboBoxSelectLocator = By.cssSelector("select[data-path='" + dataSPath + "']");

            if (isElementPresent(driver, comboBoxDivLocator)) {
                WebElement comboBoxes = wait.until(ExpectedConditions.elementToBeClickable(comboBoxDivLocator));
                comboBoxes.click();
                Thread.sleep(500);
                WebElement selector = wait.until(ExpectedConditions.visibilityOfElementLocated(comboBoxSelectLocator));
                List<WebElement> options = selector.findElements(By.tagName("option"));
                if (options.size() > 1) {
                    options.get(1).click();
                } else {
                    comboBoxes.sendKeys(Keys.ENTER);
                }
                options.clear();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            } else if (isElementPresent(driver, comboBoxSelectLocator)) {
                WebElement comboBoxes2 = wait.until(ExpectedConditions.elementToBeClickable(comboBoxSelectLocator));
                comboBoxes2.click();
                Thread.sleep(500);
                WebElement selector2 = wait.until(ExpectedConditions.visibilityOfElementLocated(comboBoxSelectLocator));
                List<WebElement> options2 = selector2.findElements(By.tagName("option"));
                if (options2.size() > 1) {
                    options2.get(1).click();
                } else {
                    comboBoxes2.sendKeys(Keys.ENTER);
                }
                comboBoxes2.clear();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error selecting the second visible combo box option: " + dataSPath );
        }
    }

    public static Optional<String> extractTabIdentifier(String href) {
        Matcher matcher = TAB_ID_PATTERN.matcher(href);
        return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
    }

    public static List<ProcessLogDTO> getProcessLogMessages() {
        return new ArrayList<>(ProcessLogFields);
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

    public static void processTabElements(WebDriver driver, List<WebElement> elements, String id) {
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
                        id
                );
            }
        }
    }
}







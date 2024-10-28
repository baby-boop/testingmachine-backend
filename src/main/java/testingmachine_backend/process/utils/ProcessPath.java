package testingmachine_backend.process.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.support.ui.*;
import testingmachine_backend.process.DTO.ProcessLogDTO;
import testingmachine_backend.process.Fields.ProcessLogFields;

import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.logging.*;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.FormFieldUtils.*;

@Slf4j
public class ProcessPath {
    private static final Logger LOGGER = Logger.getLogger(ProcessPath.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;
    private static final int LONG_WAIT_SECONDS = 90;

    @ProcessLogFields
    private static final List<ProcessLogDTO> ProcessLogFields = new ArrayList<>();

    public static void isProcessPersent(WebDriver driver, String id, String fileName) {
        try {

            LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
            boolean hasSevereError = false;

            for (LogEntry entry : logs) {

                if (entry.getLevel().toString().equals("SEVERE")) {
                    if(!entry.getMessage().contains("Uncaught TypeError: Cannot read properties of null (reading 'addClass')") && !entry.getMessage().contains("Uncaught TypeError: Cannot read properties of null (reading 'hasClass')")
                    && !entry.getMessage().contains("Failed to load resource: the server responded with a status of 404 (Not Found)"))
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

//                List<WebElement> elementsWithDataPath = findElementsWithSelector(driver, "[data-path]");

//                for (WebElement element : elementsWithDataPath) {
//                    String classAttribute = element.getAttribute("class");
//                    String valueAttribute = element.getAttribute("value");
//                    String typeAttribute = element.getAttribute("type");
//                    String dataPath = element.getAttribute("data-path");
//                    String regexData = element.getAttribute("data-regex");

//                    handleElementAction(driver, element, classAttribute, valueAttribute, typeAttribute, dataPath, regexData, id);
//                }

                detailActionButton(driver, id);

//                WebElement wfmDialog = waitForElementVisible(driver, By.cssSelector("div[id='bp-window-" + id + "']"), 10);
//                WebElement wfmSaveButton = wfmDialog.findElement(By.xpath(".//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]"));
//                wfmSaveButton.click();
                waitUtils(driver);
                if(trashMessage.isErrorMessagePresent(driver, id, fileName)){
                    log.info("Count log: " + id);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fields: " + id );

        }
    }
    private static List<WebElement> findElementsWithSelector(WebDriver driver, String cssSelector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
            List<WebElement> elements = driver.findElements(By.cssSelector(cssSelector));

            Map<String, WebElement> uniqueDataPathElements = new LinkedHashMap<>();
            for (WebElement element : elements) {
                String dataPath = element.getAttribute("data-path");
                if (!uniqueDataPathElements.containsKey(dataPath)) {
                    uniqueDataPathElements.put(dataPath, element);
                }
            }
            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector '" + cssSelector + "' not found");
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

            WebElement parent = element.findElement(By.xpath("./following-sibling::span[@class='input-group-btn']/button"));
            return parent;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Popup button not found for the input element");
            return null;
        }
    }
    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    static void clickFirstRow(WebDriver driver, String id) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));
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
                    actions.moveToElement(firstCell).doubleClick().perform();
                    waitUtils(driver);
                } else {
                    LOGGER.log(Level.INFO, "First cell in the first row not found: " + id);
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn blue-hoki btn-sm')]")));
                    closeBtn.click();
                }
            }
            rows.clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));
        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error", e);
        }
    }
    
    static void selectComboSecondOption(WebDriver driver, String dataSPath, String id) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));

            WebElement comboBoxes = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[data-s-path='" + dataSPath + "']")));
            comboBoxes.click();

            Thread.sleep(500);
            WebElement selector = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select[data-path='" + dataSPath + "']")));
            List<WebElement> options = selector.findElements(By.tagName("option"));
            if (options.size() > 1) {
                options.get(1).click();
            }

            options.clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "']")));

        } catch (Exception e) {
            System.out.println("Error selecting the second visible combo box option: " + id);
        }
    }

    private static void detailActionButton(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT_SECONDS));
        try {
            List<WebElement> elementsWithDataSectionPath = findRowElementsWithDataSectionPath(driver);

            for (WebElement element : elementsWithDataSectionPath) {
                String sectionPath = element.getAttribute("data-section-path");
                waitUtils(driver);
                List<WebElement> allActionPath = findRowActionPathsButton(driver, sectionPath);
                if(allActionPath != null ) {
                    waitUtils(driver);
                    for (WebElement action : allActionPath) {
                        String onclick = action.getAttribute("onclick");

                        if (onclick.contains("bpAddMainMultiRow")) {
                            action.click();
                            waitUtils(driver);
                            clickFirstRow(driver, id);
                            WebElement saveBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//button[contains(@class, 'btn blue btn-sm datagrid-choose-btn')]")));
                            saveBtn.click();
                            waitUtils(driver);
                            break;
                        }
                        else if (onclick.contains("bpAddMainRow")) {
                            waitUtils(driver);
                            action.click();
                            waitUtils(driver);
                            List<WebElement> rowElements = findElementsWithDetailsPath(driver, sectionPath);

                            for (WebElement rowElement : rowElements) {
                                String classAttribute = rowElement.getAttribute("class");
                                String valueAttribute = rowElement.getAttribute("value");
                                String typeAttribute = rowElement.getAttribute("type");
                                String dataPath = rowElement.getAttribute("data-path");
                                String regexData = rowElement.getAttribute("data-regex");
                                handleElementAction(driver, rowElement, classAttribute, valueAttribute, typeAttribute, dataPath, regexData, id);

                                }


                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in rows: " + id, e);
        }
    }

    private static List<WebElement> findElementsWithDetailsPath(WebDriver driver, String cssSelector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));

            List<WebElement> elements = driver.findElements(By.cssSelector(cssSelector));

            Map<String, WebElement> uniqueDataPathElements = new LinkedHashMap<>();
            for (WebElement element : elements) {
                String dataPath = element.getAttribute("data-path");
                if (!uniqueDataPathElements.containsKey(dataPath)) {
                    uniqueDataPathElements.put(dataPath, element);
                }
            }
            return new ArrayList<>(uniqueDataPathElements.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with selector '" + cssSelector + "' not found");
            return List.of();
        }
    }


    private static WebElement waitForElementVisible(WebDriver driver, By locator, int waitSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }
    private static List<WebElement> findRowElementsWithDataSectionPath(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bp-detail-body .row[data-section-path]")));
            return driver.findElements(By.cssSelector(".bp-detail-body .row[data-section-path]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Elements 'data-section-path' not found");
            return List.of();
        }
    }

    private static List<WebElement> findRowActionPathsButton(WebDriver driver, String sectionPath) {
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



    public static List<ProcessLogDTO> getProcessLogMessages() {
        return new ArrayList<>(ProcessLogFields);
    }
}







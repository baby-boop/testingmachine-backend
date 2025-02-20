package testingmachine_backend.process.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;

public class TabDetailsFieldUtils {
    static final Logger LOGGER = Logger.getLogger(TabDetailsFieldUtils.class.getName());
    public static final int SHORT_WAIT_SECONDS = 2;

    public static void tabDetailItems(WebDriver driver, String id, String fileName, String jsonId) {
        try {
            List<WebElement> tabElementItems = findTabElements(driver);
            if(tabElementItems != null ) {
                for (WebElement tab : tabElementItems) {
                    waitUtils(driver);
                    tab.click();
                    String TabHref = tab.getAttribute("href");
                    Optional<String> tabIdentifierOpt = extractTabIdentifier(TabHref);
                    String tabIdentifier = tabIdentifierOpt.get();
                    waitUtils(driver);
                    List<WebElement> elementsWithHeaderPaths = findElementsWithHeaderPath(driver, tabIdentifier);
                    processTabElements(driver, elementsWithHeaderPaths, id, fileName, jsonId,"");

                    List<WebElement> elementsWithDataSectionPath = findRowElementsWithSectionPath(driver, tabIdentifier);
                    if (elementsWithDataSectionPath != null) {
                        for (WebElement elementTab : elementsWithDataSectionPath) {
                            String sectionPath = elementTab.getAttribute("data-section-path");
                            waitUtils(driver);
                            List<WebElement> findTestElement = findTest(driver, tabIdentifier, sectionPath);
                            processTabElements(driver, findTestElement, id, fileName, jsonId,"");
                            List<WebElement> allActionTabPath = findRowActionTabPathsButton(driver, sectionPath);
                            if (allActionTabPath != null) {
                                List<WebElement> findTabDefaultRows = findRowActionTabDefaultRow(driver, sectionPath);
                                assert findTabDefaultRows != null;
                                if (findTabDefaultRows.isEmpty()) {
                                    waitUtils(driver);
                                    for (WebElement action : allActionTabPath) {
                                        String onclick = action.getAttribute("onclick");
                                        if (onclick.contains("bpAddMainMultiRow")) {
                                            action.click();
                                            waitUtils(driver);
                                            clickFirstRow(driver, id, fileName, sectionPath, "", jsonId);
                                            waitUtils(driver);
                                            List<WebElement> tabElementPaths1 = findElementsWithTabDetailsPath(driver, sectionPath, tabIdentifier);
                                            processTabElements(driver, tabElementPaths1, id, fileName, jsonId,"");
                                            break;
                                        } else if (onclick.contains("bpAddMainRow")) {
                                            waitUtils(driver);
                                            action.click();
                                            waitUtils(driver);
                                            List<WebElement> tabElementPaths = findElementsWithTabDetailsPath(driver, sectionPath, tabIdentifier);
                                            processTabElements(driver, tabElementPaths, id, fileName, jsonId,"");

//                                            List<WebElement> rowsToRowShowForms = findRowsToRowShowForm(driver, sectionPath, tabIdentifier, id);
//                                            if (rowsToRowShowForms != null) {
//                                                for (WebElement rowsToRowShowForm : rowsToRowShowForms) {
//                                                    rowsToRowShowForm.click();
//                                                    String rowsDataBPath = rowsToRowShowForm.getAttribute("data-b-path");
//                                                    List<WebElement> rowsToRowElements = findRowsToRowPaths(driver, id, tabIdentifier, sectionPath, rowsDataBPath);
//
//                                                    processTabElements(driver, rowsToRowElements, id, fileName);
//                                                    WebElement selectButton = driver.findElement(By.xpath("//button[contains(@class, 'btn green-meadow btn-sm')]"));
//                                                    selectButton.click();
//                                                }
//                                            }
                                            break;
                                        }
                                    }
                                }else{
                                    List<WebElement> findTestElement1 = findTest(driver, tabIdentifier, sectionPath);
                                    processTabElements(driver, findTestElement1, id, fileName, jsonId,"");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in tabs: " + id);
        }
    }

    public static  List<WebElement> findElementsWithHeaderPath(WebDriver driver, String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .bprocess-table-row")
            ));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] ")
            ));

            List<WebElement> elements = MainProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());

        }
        catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Elements with 'header param' and data-path not found");
            return Collections.emptyList();
        }
    }

    public static List<WebElement> findRowElementsWithSectionPath(WebDriver driver, String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='" + tabId + "'] .row[data-section-path]")));
            return driver.findElements(By.cssSelector("div[id='" + tabId + "'] .row[data-section-path]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Elements 'data-section-path' not found: " + tabId);
            return null;
        }
    }



    public static List<WebElement> findRowActionTabPathsButton(WebDriver driver, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-path='"+ sectionPath +"']")));
            return driver.findElements(By.cssSelector("div[data-section-path='"+ sectionPath +"'] button[data-action-path]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths error: " + sectionPath);
            return null;
        }
    }

    public static List<WebElement> findRowActionTabDefaultRow(WebDriver driver, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-path='"+ sectionPath +"']")));
            return driver.findElements(By.cssSelector("div[data-section-path='"+ sectionPath +"'] .tbody .bp-detail-row"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths error: " + sectionPath);
            return null;
        }
    }

    public static  List<WebElement> findElementsWithTabDetailsPath(WebDriver driver, String sectionPath, String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"'] .bp-detail-row")
            ));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"'] ")
            ));

            List<WebElement> elements = MainProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());

        }
        catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Elements with sectionPath '" + sectionPath + "' and data-path not found");
            return Collections.emptyList();
        }
    }
    public static  List<WebElement> findTest(WebDriver driver, String tabId, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"']")

            ));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"']")
//                ("div[id='bp-window-" + id + "'] div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"']")

            ));

            List<WebElement> elements = MainProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());

        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with sectionPath '"+ sectionPath +"' in data-path not found");
            return Collections.emptyList();
        }
    }


    public static List<WebElement> findTabElements(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bp-tabs .nav-tabs")));
            return driver.findElements(By.cssSelector(".bp-tabs .nav-tabs .nav-item .nav-link"));

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Tab 'Items' not found");
            return List.of();
        }
    }


    public static List<WebElement> findRowsToRowShowForm(WebDriver driver, String sectionPath, String tabId,String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + id + "'] div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"'] .bp-detail-row")));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"']")
            ));

            List<WebElement> tabElements = MainProcess.findElements(By.cssSelector("[data-b-path]"));

            for (WebElement tabElement : tabElements) {
                String tabDataPath = tabElement.getAttribute("data-b-path");
                if (tabDataPath != null) {
                    return Collections.singletonList(tabElement);
                }
            }
            return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "RowsToRowShowForm'" + sectionPath + "' selector not found");
            return Collections.emptyList();
        }
    }


    public static List<WebElement> findRowsToRowPaths(WebDriver driver, String id, String tabId, String sectionPath, String subSection) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"'] ")
            ));
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector
                    ("div[id='"+ tabId +"'] .row[data-section-path='"+ sectionPath +"'] ")
            ));
            WebElement SubProcess = MainProcess.findElement(By.cssSelector("div[data-section-path='"+ subSection +"']"));

            List<WebElement> elements = SubProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "RowsToRow path '" + id + "' not found");
            return Collections.emptyList();
        }
    }
}

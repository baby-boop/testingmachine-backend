//package testingmachine_backend.meta.Utils;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import testingmachine_backend.meta.Service.MetaMessageStatusService;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class CheckWorkflow {
//
//    private static final Logger LOGGER = Logger.getLogger(CheckWorkflow.class.getName());
//    private static final int SHORT_WAIT_SECONDS = 2;
//    private static final int LONG_WAIT_MILLISECONDS = 1000;
//    private static int workflowCount = 0;
//
//    public static boolean isErrorMessagePresent(WebDriver driver,  String fileName, String id, String code, String name, String jsonId) {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//            WebElement workflowTitle = waitForElementVisible(driver, By.xpath("//button[contains(text(),'Төлөв өөрчлөх')]"), SHORT_WAIT_SECONDS);
//            if (workflowTitle == null) {
//                return false;
//            }
//
//            workflowCount++;
//
//            List<WebElement> rows = findRows(driver, fileName, id, code, name, jsonId);
//            boolean foundWorkflowId = false;
//            LOGGER.info("Number of rows found: " + rows.size());
//
//            for (WebElement row : rows) {
//                clickFirstRow(driver, row);
//                workflowTitle.click();
//
//                WebElement dropdownMenu = waitForElementVisible(driver, By.cssSelector(".dropdown-menu.workflow-dropdown-" + id + ".show"), 10);
//
//                if (dropdownMenu != null) {
//                    foundWorkflowId = clickValidMenuOption(driver, wait, dropdownMenu, fileName,id, code, name, jsonId);
//                }
//
//                if (foundWorkflowId) {
//                    break;
//                }
//                Thread.sleep(SHORT_WAIT_SECONDS);
//            }
//
//
//            return foundWorkflowId;
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error workflow: {0}", e.getMessage());
//
//            return false;
//        }
//    }
//
//    public static int getWorkflowCount() {
//        return workflowCount;
//    }
//
//
//
//    private static List<WebElement> findRows(WebDriver driver,String fileName, String id, String code, String name, String jsonId) {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(@id,'datagrid-row-r1-1')]")));
//            return driver.findElements(By.xpath("//tr[contains(@id,'datagrid-row-r1-1')]"));
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Rows not found", e.getMessage());
//            NoDataLogger.logError(fileName, id, code, name, jsonId);
//            return new ArrayList<>();
//        }
//    }
//
//    private static void clickFirstRow(WebDriver driver, WebElement row) throws InterruptedException {
//        Actions actions = new Actions(driver);
//
//        WebElement cell = row.findElement(By.xpath(".//td[1]"));
//        if(cell != null){
//            scrollToElement(driver, cell);
//            actions.moveToElement(cell).click().perform();
//            Thread.sleep(LONG_WAIT_MILLISECONDS);
//        }else{
//            LOGGER.log(Level.INFO, "Not found data");
//        }
//
//    }
//
//    private static boolean clickValidMenuOption(WebDriver driver, WebDriverWait wait, WebElement dropdownMenu, String fileName, String id, String code, String name, String jsonId) {
//        List<WebElement> listItems = dropdownMenu.findElements(By.tagName("li"));
//
//        for (WebElement listItem : listItems) {
//            WebElement anchorTag = listItem.findElement(By.tagName("a"));
//            String onclickAttr = anchorTag.getAttribute("onclick");
//            if (onclickAttr != null && onclickAttr.contains("changeWfmStatusId")) {
//                String itemText = anchorTag.getText();
//                LOGGER.log(Level.INFO, "Clicking the list item with text: {0}", itemText);
//                scrollToElement(driver, anchorTag);
//                anchorTag.click();
//
//                WebElement wfmDialog = waitForElementVisible(driver, By.cssSelector("div[aria-describedby='dialog-changeWfmStatus-" + id + "']"), 10);
//                if (wfmDialog != null) {
//                    WebElement wfmSaveButton = wfmDialog.findElement(By.xpath("//button[contains(text(),'Хадгалах')]"));
//                    wfmSaveButton.click();
//
//                    if(checkForMessages(driver, wait, fileName ,id, code, name, jsonId)){
//                        return true;
//                    }
//                }
//                return true;
//            } else {
//                LOGGER.log(Level.INFO, "Not found next workflow: {0}", id);
//                break;
//            }
//        }
//
//        return false;
//    }
//
//    private static boolean checkForMessages(WebDriver driver, WebDriverWait wait,  String moduleName, String id, String code, String name, String jsonId) {
//        try {
//            WebElement messageContainer = waitForElementVisible(driver, By.cssSelector(".brighttheme.ui-pnotify-container"), SHORT_WAIT_SECONDS);
//            if (messageContainer != null) {
//                WebElement messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title"));
//                String messageTitleText = messageTitle.getText().toLowerCase();
//                return processMessage(driver, messageTitleText,moduleName, id, code, name, jsonId);
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error while checking messages: {0}", e.getMessage());
//        }
//        return false;
//    }
//
//    private static boolean processMessage(WebDriver driver, String messageTitleText, String id, String moduleName, String code, String name, String jsonId) {
//        if (messageTitleText.contains("warning") || messageTitleText.contains("Warning") || messageTitleText.contains("error") || messageTitleText.contains("Error") ||
//                messageTitleText.contains("info") || messageTitleText.contains("Info")) {
//
//            if (messageTitleText.contains("error fetching http headers")) {
//                LOGGER.log(Level.SEVERE, "Connection error! Last processed: {0} - {1}", new Object[]{moduleName, id});
//                driver.quit();
//                return false;
//            } else {
//                return extractErrorMessage(driver, moduleName, id,  code, name, jsonId);
//            }
//        }
//        return false;
//    }
//
//    private static boolean extractErrorMessage(WebDriver driver, String moduleName, String id, String code, String name, String jsonId) {
//        try {
//            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
//            WebElement messageContent = waitForElementVisible(driver, By.cssSelector(".ui-pnotify-text"), SHORT_WAIT_SECONDS);
//            if (messageContent != null) {
//                String messageText = messageContent.getText();
//                MetaMessageStatusService.addMetaStatus(moduleName, id, code, name, "worflow", messageText, jsonId);
//                return true;
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error while extracting message: {0}", e.getMessage());
//        }
//        return false;
//    }
//
//    private static String getMetaAttribute(WebDriver driver, String attribute) {
//        try {
//            WebElement targetDiv = driver.findElement(By.cssSelector("div.main-dataview-container"));
//            return targetDiv.getAttribute(attribute);
//        } catch (Exception e) {
//            LOGGER.log(Level.WARNING, "Meta attribute not found: {0}", attribute);
//            return "";
//        }
//    }
//
//    private static WebElement waitForElementVisible(WebDriver driver, By locator, int waitSeconds) {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
//            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//        } catch (Exception e) {
////            LOGGER.log(Level.SEVERE, "Element not found: {0}", locator);
//            return null;
//        }
//    }
//
//    private static void scrollToElement(WebDriver driver, WebElement element) {
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
//    }
//
//}

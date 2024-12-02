//package testingmachine_backend.meta.MetaList;
//
//import org.openqa.selenium.*;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import testingmachine_backend.meta.Controller.ListConfig;
//import testingmachine_backend.meta.DTO.ErrorTimeoutDTO;
//import testingmachine_backend.meta.Fields.ErrorTimeoutField;
//import testingmachine_backend.meta.Utils.CheckWorkflow;
//import testingmachine_backend.meta.Utils.ErrorLogger;
//import testingmachine_backend.meta.Utils.IsErrorList;
//
//import java.io.File;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
//import static testingmachine_backend.meta.Utils.FileUtils.readIdsFromFile;
//
//public class MetaWithWorkflow {
//    private final WebDriver driver;
//    private static int metaCount = 0;
//    private static int totaMetaCount = 0;
//
//    @ErrorTimeoutField
//    private final static List<ErrorTimeoutDTO> errorTimeoutMessages = new ArrayList<>();
//
//
//    public MetaWithWorkflow(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    public void mainList() {
//        try {
//            WebDriverWait wait = ListConfig.getWebDriverWait(driver);
//            driver.get(ListConfig.LoginUrl);
//            driver.manage().window().setSize(new Dimension(1500, 800));
//
//            Thread.sleep(500);
//
//            WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
//            Select dbSelect = new Select(selectDb);
//            dbSelect.selectByValue("dXgxZERkUjNzSkFhZVc1aUU2dTBNQT09");
//
//            // Login Process
//            WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
//            userNameField.sendKeys(ListConfig.USERNAME);
//
//            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
//            passwordField.sendKeys(ListConfig.PASSWORD);
//            passwordField.sendKeys(Keys.ENTER);
//
////            Thread.sleep(3000);
//
//            WebElement clickThat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'login/connectClient')]//h6[text()='Хишиг-Арвин Групп']")));
////            WebElement clickThat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'login/connectClient')]//h6[text()='Хишиг Арвин Индустриал']")));
//            clickThat.click();
//
//            Thread.sleep(2000);
//
//            String directoryPath = "C:\\Users\\batde\\Downloads\\Hishig arvin uat lookupIds";
//
//            File folder = new File(directoryPath);
//            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));
//
//            if (listOfFiles != null) {
//
//                List<String> allIds = new ArrayList<>();
//
//                for (File file : listOfFiles) {
//                    List<String> idsFromFile = readIdsFromFile(file.getAbsolutePath());
//                    allIds.addAll(idsFromFile);
//                }
//
//                int totalIds = allIds.size();
//                totaMetaCount = totalIds;
//                System.out.println("Total IDs to meta: " + totalIds);
//
//                for (File file : listOfFiles) {
//                    System.out.println("Processing file: " + file.getName());
//
//                    List<String> ids = readIdsFromFile(file.getAbsolutePath());
//
//                    for (String id : ids) {
//                        String url = ListConfig.MainUrl + id;
//                        driver.get(url);
//                        driver.navigate().refresh();
//
//                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
//                        retryWaitForLoadToDisappear(driver, file.getName(), id);
//                        retryWaitForLoadingToDisappear(driver, file.getName(), id);
//                        if (IsErrorList.isErrorMessagePresent(driver, id, file.getName())) {
//                            System.out.println("Error found in ID: " + id);
//                        }
//                        if(CheckWorkflow.isErrorMessagePresent(driver, id, file.getName())) {
//                            System.out.println("Workflow in ID: " + id);
//                        }
//                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
//                        retryWaitForLoadToDisappearForWorkflow(driver);
//                        retryWaitForLoadingToDisappearForWorkflow(driver);
//                        metaCount++;
//                        System.out.println("Count: " + metaCount + ", ID: " + id);
//                    }
//                }
//            } else {
//                System.err.println("No .txt files found in directory: " + directoryPath);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//    }
//
//
//    public static List<ErrorTimeoutDTO> errorTimeoutMessages() {
//        return ErrorLogger.getErrorTimeoutMessages();
//    }
//
//    public static int getCheckCount() {
//        return metaCount;
//    }
//
//    public static int getTotalCount() {
//        return totaMetaCount;
//    }
//
//    private WebElement retryFindElement(WebDriverWait wait, By by, int attempts) {
//        int retryCount = 0;
//        while (retryCount < attempts) {
//            try {
//                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
//            } catch (StaleElementReferenceException | NoSuchElementException e) {
//                retryCount++;
//                System.out.println("Retrying element lookup: " + by.toString() + " (attempt " + retryCount + ")");
//            }
//        }
//        throw new NoSuchElementException("Failed to find element after " + attempts + " attempts: " + by.toString());
//    }
//
//    private void retryWaitForLoadingToDisappear(WebDriver driver, String fileName, String id) {
//        retryAction(() -> waitForLoadingToDisappear(driver, fileName, id), 3);
//    }
//
//    private void retryWaitForLoadToDisappear(WebDriver driver, String fileName, String id) {
//        retryAction(() -> waitForLoadToDisappear(driver, fileName, id), 3);
//    }
//
//    private void retryWaitForLoadingToDisappearForWorkflow(WebDriver driver) {
//        retryAction(() -> waitForLoadingToDisappearForWorkflow(driver), 3);
//    }
//
//    private void retryWaitForLoadToDisappearForWorkflow(WebDriver driver) {
//        retryAction(() -> waitForLoadToDisappearForWorkflow(driver), 3);
//    }
//
//    private static void waitForLoadingToDisappear(WebDriver driver, String fileName, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
//        try {
//            WebElement loadingMessage = driver.findElement(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']"));
//            if (loadingMessage.isDisplayed()) {
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']")));
//            }
//        } catch (NoSuchElementException e) {
//            // Loading message not found, proceed
//        } catch (TimeoutException e) {
//            ErrorLogger.logError(fileName, id);
//        }
//    }
//
//    private static void waitForLoadToDisappear(WebDriver driver, String fileName, String id) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
//        try {
//            WebElement loadingMessages = driver.findElement(By.cssSelector("div.loading-message.loading-message-boxed"));
//            if (loadingMessages.isDisplayed()) {
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-message.loading-message-boxed")));
//            }
//        } catch (NoSuchElementException e) {
//            // Loading message not found, proceed
//        } catch (TimeoutException e) {
//            ErrorLogger.logError(fileName, id);
//        }
//    }
//
//
//    private static void waitForLoadingToDisappearForWorkflow(WebDriver driver) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
//        try {
//            WebElement loadingMessage = driver.findElement(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']"));
//            if (loadingMessage.isDisplayed()) {
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']")));
//            }
//        } catch (NoSuchElementException e) {
//            // Loading message not found, proceed
//        } catch (TimeoutException e) {
//        }
//    }
//
//    private static void waitForLoadToDisappearForWorkflow(WebDriver driver) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
//        try {
//            WebElement loadingMessages = driver.findElement(By.cssSelector("div.loading-message.loading-message-boxed"));
//            if (loadingMessages.isDisplayed()) {
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-message.loading-message-boxed")));
//            }
//        } catch (NoSuchElementException e) {
//            // Loading message not found
//        } catch (TimeoutException e) {
//
//        }
//    }
//    // Retry mechanism for an action
//    private void retryAction(Runnable action, int maxAttempts) {
//        int attempt = 0;
//        while (attempt < maxAttempts) {
//            try {
//                action.run();
//                return;
//            } catch (StaleElementReferenceException e) {
//                attempt++;
//                System.out.println("Retrying action (attempt " + attempt + ")");
//            }
//        }
//    }
//}

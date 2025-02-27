package testingmachine_backend.projects.meta.Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    public static int waitTime = 120;


    public static void retryWaitForLoadingToDisappear(WebDriver driver, String fileName, String id, String code, String name, String jsonId, String type, int totalCount, String customerName, int maxAttempts) {
        retryAction(() -> waitForLoadingToDisappear(driver, fileName, id, code, name, jsonId, type, totalCount, customerName), maxAttempts);
    }
    public static void retryWaitForLoadToDisappear(WebDriver driver, String fileName, String id, String code, String name, String jsonId, String type, int totalCount, String customerName,int maxAttempts) {
        retryAction(() -> waitForLoadToDisappear(driver, fileName, id, code, name, jsonId, type, totalCount, customerName), maxAttempts);
    }
    public static void retryWaitForLoadForWorkflow(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForLoadForWorkflow(driver), maxAttempts);
    }

    public static void retryWaitForLoadingForWorkflow(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForLoadingForWorkflow(driver), maxAttempts);
    }

    private static void waitForLoadingToDisappear(WebDriver driver, String fileName, String id, String code, String name, String jsonId, String type, int totalCount, String customerName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        try {
            WebElement loadingMessage = driver.findElement(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']"));
            if (loadingMessage.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
        } catch (TimeoutException e) {
            ErrorLogger.logError(fileName, id, code, name, jsonId, type, totalCount, customerName);
        }
    }

    private static void waitForLoadToDisappear(WebDriver driver, String fileName, String id, String code, String name, String jsonId, String type, int totalCount, String customerName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        try {
            WebElement loadingMessages = driver.findElement(By.cssSelector("div.loading-message.loading-message-boxed"));
            if (loadingMessages.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-message.loading-message-boxed")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
        } catch (TimeoutException e) {
            ErrorLogger.logError(fileName, id, code, name, jsonId, type, totalCount, customerName);
        }
    }

    public static void waitForLoadingForWorkflow(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            WebElement loadingMessage = driver.findElement(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']"));
            if (loadingMessage.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found
        } catch (TimeoutException e) {
        }
    }

    public static void waitForLoadForWorkflow(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            WebElement loadingMessages = driver.findElement(By.cssSelector("div.loading-message.loading-message-boxed"));
            if (loadingMessages.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-message.loading-message-boxed")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found
        } catch (TimeoutException e) {

        }
    }

    private static void retryAction(Runnable action, int maxAttempts) {
        int attempt = 0;
        while (attempt < maxAttempts) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                attempt++;
                System.out.println("Retrying action (" + attempt + ")");
            }
        }
    }
}

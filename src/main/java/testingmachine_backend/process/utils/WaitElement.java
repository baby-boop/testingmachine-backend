package testingmachine_backend.process.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitElement {

    public static int TIMEOUT = 5;

    public static void retryWaitForLoadingToDisappear(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForLoadingToDisappear(driver), maxAttempts);
    }
    public static void retryWaitForLoadToDisappear(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForLoadToDisappear(driver), maxAttempts);
    }
    public static void retryWaitForSpinner(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForSpinner(driver), maxAttempts);
    }

    public static void retryWaitForBlockUIDisappear(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForBlockUIDisappear(driver), maxAttempts);
    }

    private static void waitForBlockUIDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        try {
            WebElement blockUI = driver.findElement(By.cssSelector("div.blockUI.blockMsg.blockPage"));
            if (blockUI.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockMsg.blockPage")));
            }
        } catch (NoSuchElementException e) {
            // Block UI not found, proceed
        } catch (TimeoutException e) {
            // Handle timeout case if needed
            System.out.println("Timeout waiting for Block UI to disappear.");
        }
    }

    private static void waitForLoadingToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        try {
            WebElement loadingMessage = driver.findElement(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']"));
            if (loadingMessage.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
        } catch (TimeoutException e) {
            //
        }
    }

    private static void waitForLoadToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        try {
            WebElement loadingMessages = driver.findElement(By.cssSelector("div.loading-message.loading-message-boxed"));
            if (loadingMessages.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-message.loading-message-boxed")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
        } catch (TimeoutException e) {
            //
        }
    }

    private static void waitForSpinner(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        try {

            WebElement saveButtonSpinner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]//i[contains(@class, 'fa fa-spinner fa-pulse fa-fw')]")));
            if (saveButtonSpinner.isDisplayed()) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class, 'btn btn-sm btn-circle btn-success bpMainSaveButton bp-btn-save ')]//i[contains(@class, 'fa fa-spinner fa-pulse fa-fw')]")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
        }catch (TimeoutException e) {
            //
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

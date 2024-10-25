package testingmachine_backend.process.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.meta.Utils.ErrorLogger;

import java.time.Duration;

public class WaitElement {

    public static int waitTime = 120;


    public static void retryWaitForLoadingToDisappear(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForLoadingToDisappear(driver), maxAttempts);
    }
    public static void retryWaitForLoadToDisappear(WebDriver driver, int maxAttempts) {
        retryAction(() -> waitForLoadToDisappear(driver), maxAttempts);
    }

    private static void waitForLoadingToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        try {
            WebElement loadingMessage = driver.findElement(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']"));
            if (loadingMessage.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
        } catch (TimeoutException e) {

        }
    }

    private static void waitForLoadToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        try {
            WebElement loadingMessages = driver.findElement(By.cssSelector("div.loading-message.loading-message-boxed"));
            if (loadingMessages.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-message.loading-message-boxed")));
            }
        } catch (NoSuchElementException e) {
            // Loading message not found, proceed
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

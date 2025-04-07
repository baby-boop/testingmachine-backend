package testingmachine_backend.projects.product;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitElementProduct {

    public static int TIMEOUT = 20;

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
        By selector = By.cssSelector("div.blockUI.blockMsg.blockPage");
        try {
            WebElement blockUI = driver.findElement(selector);
            if (blockUI.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
            }
        } catch (NoSuchElementException e) {
            // Not found, continue
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for Block UI. Removing manually.");
            removeElement(driver, selector);
        }
    }

    private static void waitForLoadingToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        By selector = By.xpath("//div[contains(@class, 'datagrid-mask-msg') and text()='Түр хүлээнэ үү']");
        try {
            WebElement loadingMessage = driver.findElement(selector);
            if (loadingMessage.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
            }
        } catch (NoSuchElementException e) {
            // Not found
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for loading message. Removing manually.");
            removeElement(driver, selector);
        }
    }

    private static void removeElement(WebDriver driver, By selector) {
        try {
            WebElement element = driver.findElement(selector);
            ((JavascriptExecutor) driver).executeScript("arguments[0].remove();", element);
        } catch (NoSuchElementException ignored) {
            // Already gone
        }
    }



    private static void waitForLoadToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        By selector = By.cssSelector("div.loading-message.loading-message-boxed");
        try {
            WebElement loadingMessages = driver.findElement(selector);
            if (loadingMessages.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
            }
        } catch (NoSuchElementException e) {
            // Not found
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for boxed loading. Removing manually.");
            removeElement(driver, selector);
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

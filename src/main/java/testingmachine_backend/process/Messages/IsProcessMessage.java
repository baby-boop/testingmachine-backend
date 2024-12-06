package testingmachine_backend.process.Messages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.Service.ProcessMessageStatusService;

import java.time.Duration;

public class IsProcessMessage {


    @Getter
    private static int warningCount = 0;
    @Getter
    private static int errorCount = 0;
    @Getter
    private static int infoCount = 0;
    @Getter
    private static int successCount = 0;

    private static final int SHORT_WAIT_SECONDS = 10;

    public static boolean isErrorMessagePresent(WebDriver driver, String id, String code, String name, String systemName, String TestProcessType) {
        try {
            WebElement messageContainer = waitForElement(driver, By.cssSelector(".brighttheme.ui-pnotify-container"), SHORT_WAIT_SECONDS);
            String messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title")).getText().toLowerCase();

            if (messageTitle.contains("warning")) {
                return processMessage(driver, "warning", id, code, name, systemName, TestProcessType);
            } else if (messageTitle.contains("error")) {
                return processMessage(driver, "error", id, code, name, systemName, TestProcessType);
            } else if (messageTitle.contains("success")) {
                return processMessage(driver, "success", id, code, name, systemName, TestProcessType);
            }   else if (messageTitle.contains("info")) {
                return processMessage(driver, "info", id, code, name, systemName, TestProcessType);
            }
            return false;
        } catch (Exception e) {
            System.out.println("No alert found for process: " + id);
            return false;
        }
    }

    private static boolean processMessage(WebDriver driver, String type, String id, String code, String name, String systemName, String TestProcessType) {
        try {
            WebElement messageContent = waitForElement(driver, By.cssSelector(".ui-pnotify-text"), 2);
            String messageText = messageContent.getText();

            switch (type) {
                case "warning":
                    warningCount++;
                    break;
                case "error":
                    errorCount++;
                    break;
                case "success":
                    successCount++;
                    break;
                case "info":
                    infoCount++;
                    break;
            }

            ProcessMessageStatusService.addProcessStatus(systemName, id, code, name, type, messageText, TestProcessType);

            return true;
        } catch (Exception e) {
            System.out.println("Error extracting message for process: " + id);
            return false;
        }
    }

    private static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

}
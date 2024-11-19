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

    public static boolean isErrorMessagePresent(WebDriver driver, String id, String fileName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
            WebElement messageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".brighttheme.ui-pnotify-container")));
            WebElement messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title"));
            String messageTitleText = messageTitle.getText().toLowerCase();

            if (messageTitleText.contains("warning") ) {
                return extractErrorMessage(driver, true, false, false, false, id , fileName);
            } else if (messageTitleText.contains("error") ) {
                return extractErrorMessage(driver, false, true, false, false, id, fileName);
            } else if (messageTitleText.contains("success")) {
                return extractErrorMessage(driver,  false, false, true, false,  id, fileName);
            } else if (messageTitleText.contains("info")) {
                return extractErrorMessage(driver,  false, false, false, true, id, fileName);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Not found alert: " + id);
            return false;
        }
    }

    private static boolean extractErrorMessage(WebDriver driver,  boolean isWarning, boolean isError, boolean isSuccess, boolean isInfo, String id, String fileName) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement messageContent = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
            String messageText = messageContent.getText();


            if (isWarning) {
                warningCount++;
                ProcessMessageStatusService.addProcessStatus(fileName, id, "warning", messageText);
            } else if (isError) {
                errorCount++;
                ProcessMessageStatusService.addProcessStatus(fileName, id, "error", messageText);
            } else if (isInfo) {
                infoCount++;
                ProcessMessageStatusService.addProcessStatus(fileName, id, "info", messageText);
            } else if(isSuccess) {
                successCount++;
                ProcessMessageStatusService.addProcessStatus(fileName, id, "success", messageText);
            }

            return messageContent.isDisplayed();

        } catch (Exception e) {
            System.out.println("Error message for extracting : " + id);
            return false;
        }
    }

}
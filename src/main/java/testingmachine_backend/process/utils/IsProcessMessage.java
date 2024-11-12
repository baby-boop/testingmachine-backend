package testingmachine_backend.process.utils;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.DTO.ErrorMessageDTO;
import testingmachine_backend.process.DTO.InfoMessageDTO;
import testingmachine_backend.process.DTO.WarningMessageDTO;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class IsProcessMessage {

    private static final List<ErrorMessageDTO> ErrorMessageField = new ArrayList<>();
    private static final List<WarningMessageDTO> WarningMessageField = new ArrayList<>();
    private static final List<InfoMessageDTO> InfoMessageField = new ArrayList<>();
    @Getter
    private static int warningCount = 0;
    @Getter
    private static int errorCount = 0;
    @Getter
    private static int infoCount = 0;
    @Getter
    private static int successCount = 0;

    private static final int SHORT_WAIT_SECONDS = 1;


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

            WarningMessageDTO warningMessages = new WarningMessageDTO(fileName, id, messageText);
            ErrorMessageDTO errorMessages = new ErrorMessageDTO(fileName, id, messageText);
            InfoMessageDTO infoMessages = new InfoMessageDTO(fileName, id, messageText);

            if (isWarning) {
                warningCount++;
                WarningMessageField.add(warningMessages);
            } else if (isError) {
                errorCount++;
                ErrorMessageField.add(errorMessages);
            } else if (isInfo) {
                infoCount++;
                InfoMessageField.add(infoMessages);
            } else if(isSuccess) {
                successCount++;
            }

            return messageContent.isDisplayed();

        } catch (Exception e) {
            System.out.println("Error message for extracting : " + id);
            return false;
        }
    }

    public static List<WarningMessageDTO> getProcessWarningMessages() {
        return new ArrayList<>(WarningMessageField);
    }

    public static List<ErrorMessageDTO> getProcessErrorMessages() {
        return new ArrayList<>(ErrorMessageField);
    }

    public static List<InfoMessageDTO> getProcessInfoMessages() {
        return new ArrayList<>(InfoMessageField);
    }
}
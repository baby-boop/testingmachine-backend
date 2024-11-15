package testingmachine_backend.process.Messages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.DTO.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PopupMessage {

    private static final List<PopupMessageDTO> PopupMessageField = new ArrayList<>();

    private static final int SHORT_WAIT_SECONDS = 1;

    public static boolean isErrorMessagePresent(WebDriver driver, String datapath, String id, String fileName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
            WebElement messageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".brighttheme.ui-pnotify-container")));
            WebElement messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title"));
            String messageTitleText = messageTitle.getText().toLowerCase();
            if (messageTitleText.contains("error") ) {
                return extractErrorMessage(driver, datapath, id, fileName);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Not found alert: " + id);
            return false;
        }
    }

    private static boolean extractErrorMessage(WebDriver driver, String datapath, String id, String fileName) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement messageContent = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
            String messageText = messageContent.getText();

            PopupMessageDTO popupMessages = new PopupMessageDTO(fileName, id, datapath, messageText);

            PopupMessageField.add(popupMessages);

            return messageContent.isDisplayed();

        } catch (Exception e) {
            System.out.println("Error message for extracting : " + id);
            return false;
        }
    }
    public static List<PopupMessageDTO> getUniquePopupMessages() {
        Set<PopupMessageDTO> uniqueData = new LinkedHashSet<>(PopupMessageField);
        return new ArrayList<>(uniqueData);
    }
}

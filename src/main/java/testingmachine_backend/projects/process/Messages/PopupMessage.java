package testingmachine_backend.projects.process.Messages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.projects.process.DTO.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PopupMessage {

    @testingmachine_backend.projects.process.Fields.PopupMessageField
    public static final ThreadLocal<List<PopupMessageDTO>> PopupMessageField = ThreadLocal.withInitial(ArrayList::new);

    private static final int SHORT_WAIT_SECONDS = 1;

    public static boolean isErrorMessagePresent(WebDriver driver, String datapath, String id, String fileName, String jsonId) {
        try {
            WebElement messageContainer = waitForElement(driver, By.cssSelector(".brighttheme.ui-pnotify-container"), SHORT_WAIT_SECONDS);
            String messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title")).getText().toLowerCase();

            if (messageTitle.contains("warning") || messageTitle.contains("alert")) {
                return extractErrorMessage(driver, datapath, id, fileName, jsonId);
            }else if (messageTitle.contains("error")) {
                return extractErrorMessage(driver, datapath, id, fileName, jsonId);
            }else if (messageTitle.contains("success")) {
                return extractErrorMessage(driver, datapath, id, fileName, jsonId);
            }else if (messageTitle.contains("info")) {
                return extractErrorMessage(driver, datapath, id, fileName, jsonId);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean extractErrorMessage(WebDriver driver, String datapath, String id, String fileName, String jsonId) {
        try {

            WebElement messageContent = waitForElement(driver, By.cssSelector(".ui-pnotify-text"), 2);
            String messageText = messageContent.getText();

            PopupMessageDTO popupMessages = new PopupMessageDTO(fileName, id, datapath, messageText, jsonId);

            PopupMessageField.get().add(popupMessages);

            return messageContent.isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }
    public static List<PopupMessageDTO> getUniquePopupMessages() {
        Set<PopupMessageDTO> uniqueData = new LinkedHashSet<>(PopupMessageField.get());
        return new ArrayList<>(uniqueData);
    }

    private static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}

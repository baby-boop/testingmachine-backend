package testingmachine_backend.meta.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import testingmachine_backend.meta.Service.MetaMessageStatusService;

import java.time.Duration;

public class IsErrorMessage {

    private static final Logger logger = LogManager.getLogger(IsErrorMessage.class);

    public static boolean isErrorMessagePresent(WebDriver driver, String id, String moduleName, String code, String name, String jsonId) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            WebElement messageContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[id='objectdatagrid-" + id + "']")));
            String tagName = messageContainer.getTagName();

            if ("table".equals(tagName)) {

                WebElement messageContainer1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".brighttheme.ui-pnotify-container")));
                WebElement messageTitle = messageContainer1.findElement(By.cssSelector(".ui-pnotify-title"));
                String messageTitleText = messageTitle.getText().toLowerCase();

                if (messageTitleText.contains("warning") || messageTitleText.contains("error") || messageTitleText.contains("info")) {
                    WebElement connectionERROR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
                    String connectionError = connectionERROR.getText();
                    if (connectionError.contains("Error Fetching http headers")) {
                        logger.warn("Холболтоо шалгана уу! Сүүлд ажилласан: {} - {}", moduleName, id);
                        driver.quit();
                    } else {
                        return extractErrorMessage(driver, id, moduleName, code, name, tagName, jsonId);
                    }
                }
                return false;

            } else if ("div".equals(tagName)) {

                WebElement messageContainer1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='objectdatagrid-" + id + "']")));
                WebElement messageTitle = messageContainer1.findElement(By.cssSelector(".alert-danger"));
                String messageTitleText = messageTitle.getText().toLowerCase();

                if (messageTitleText.contains("error message")) {
                    return extractErrorMessage(driver, id, moduleName, code, name, tagName, jsonId);
                }
                return false;
            }
            return false;
        } catch (Exception e) {
//            logger.error("Error in isErrorMessagePresent: ", e);
            return false;
        }
    }

    private static boolean extractErrorMessage(WebDriver driver, String id, String moduleName, String code, String name, String type, String jsonId) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));

            if ("table".equals(type)) {
                WebElement messageContent = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
                String messageText = messageContent.getText();
                MetaMessageStatusService.addMetaStatus(moduleName, id, code, name, "error", messageText, jsonId);

                return messageContent.isDisplayed();
            } else if ("div".equals(type)) {
                WebElement messageContainer = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='objectdatagrid-" + id + "']")));
                WebElement messageContent = messageContainer.findElement(By.cssSelector(".alert-danger"));
                String messageText = messageContent.getText();

                MetaMessageStatusService.addMetaStatus(moduleName, id, code, name, "error", messageText, jsonId);

                return messageContent.isDisplayed();
            }

            return false;
        } catch (Exception e) {
            logger.error("Error while extracting message: ", e);
            return false;
        }
    }
}

package testingmachine_backend.projects.metaWithProcess.Controller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IsMetaErrorList {

    public static boolean isErrorMessagePresent(WebDriver driver, String id, String moduleName, String code, String name, String jsonId) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            WebElement messageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".brighttheme.ui-pnotify-container")));
            WebElement messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title"));
            String messageTitleText = messageTitle.getText().toLowerCase();

            if (messageTitleText.contains("warning") || messageTitleText.contains("Warning") ||
                    messageTitleText.contains("error") || messageTitleText.contains("Error") ||
                    messageTitleText.contains("info") || messageTitleText.contains("Info"))
            {
                WebElement connectionERROR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
                String connectionError = connectionERROR.getText();
                if(connectionError.contains("Error Fetching http headers")){
                    System.out.println("Холболтоо шалгана уу!" );
                    System.out.println("Сүүлд ажилласан: " + moduleName + " - " + id);
                    driver.quit();
                }
                else{
                    return extractErrorMessage(driver,  id, moduleName , code , name, jsonId);
                }
            }

            return false;
        } catch (Exception e) {
//            System.out.println("Error while checking for message title: " + e.getMessage());
            return false;
        }
    }

    private static boolean extractErrorMessage(WebDriver driver, String id, String moduleName, String code, String name, String jsonId) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            WebElement messageContent = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
            String messageText = messageContent.getText();

            MetaWithProcessStatusService.addMetaStatus(moduleName, id, code, name, "error", messageText, jsonId);

            return messageContent.isDisplayed();
        } catch (Exception e) {
            System.out.println("Error while extracting message: " + e.getMessage());
            return false;
        }
    }
}

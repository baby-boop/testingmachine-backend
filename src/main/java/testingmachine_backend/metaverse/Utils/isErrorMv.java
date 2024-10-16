package testingmachine_backend.metaverse.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.metaverse.DTO.MVerrorMessageDTO;
import testingmachine_backend.metaverse.Fields.MvMessageField;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class isErrorMv {

    @MvMessageField
    private static List<MVerrorMessageDTO> MvMessageField = new ArrayList<>();

    public static boolean isErrorMessagePresent(WebDriver driver, String id,String menuId, String fileName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
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
                    System.out.println("Сүүлд ажилласан: " + fileName + " - " + id +  "  menu id:" + menuId);
                    driver.quit();
                }
                else{
                    return extractErrorMessage(driver,  id, menuId, fileName);
                }
            }

            return false;
        } catch (Exception e) {
//            System.out.println("Error while checking for message title: " + e.getMessage());
            return false;
        }
    }

    private static boolean extractErrorMessage(WebDriver driver, String id, String menuId, String fileName) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement messageContent = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
            String messageText = messageContent.getText();

            String metaId = "";
            String metaCode  = "";
            try {
                WebElement targetDiv = driver.findElement(By.cssSelector("div.main-dataview-container"));
                metaId = targetDiv.getAttribute("data-process-id");
                metaCode = targetDiv.getAttribute("data-meta-code");
            } catch (Exception e) {
                System.out.println("Data meta ID not found: " + e.getMessage());
            }

            MVerrorMessageDTO mvErrorMessageDTO = new MVerrorMessageDTO(fileName, metaId, metaCode, messageText);

            MvMessageField.add(mvErrorMessageDTO);

            return messageContent.isDisplayed();
        } catch (Exception e) {
            System.out.println("Error while extracting message: " + e.getMessage());
            return false;
        }
    }

    public static List<MVerrorMessageDTO> getMvMessages() {
        return new ArrayList<>(MvMessageField);
    }
}

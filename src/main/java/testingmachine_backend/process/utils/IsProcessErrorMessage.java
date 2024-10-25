//package testingmachine_backend.process.utils;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import testingmachine_backend.process.DTO.ProcessErrorMessageDTO;
//import testingmachine_backend.process.Fields.ProcessErrorMessageField;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
//public class IsProcessErrorMessage {
//
//    @ProcessErrorMessageField
//    private static final List<ProcessErrorMessageDTO> ProcessErrorMessageField = new ArrayList<>();
//
//    public static boolean isErrorMessagePresent(WebDriver driver, String id, String fileName) {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
//            WebElement messageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".brighttheme.ui-pnotify-container")));
//            WebElement messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title"));
//            String messageTitleText = messageTitle.getText().toLowerCase();
//
//
//            if (messageTitleText.contains("warning") || messageTitleText.contains("Warning") ||
//                    messageTitleText.contains("error") || messageTitleText.contains("Error") ||
//                    messageTitleText.contains("info") || messageTitleText.contains("Info"))
//            {
//                WebElement connectionERROR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
//                String connectionError = connectionERROR.getText();
//                if(connectionError.contains("Error Fetching http headers")){
//                    System.out.println("Холболтоо шалгана уу!" );
//                    System.out.println("Сүүлд ажилласан: " + fileName + " - " + id);
//                    driver.quit();
//                }
//                else{
//                    return extractErrorMessage(driver,  id, fileName);
//                }
//            }
//
//            return false;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private static boolean extractErrorMessage(WebDriver driver, String id, String fileName) {
//        try {
//            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
//            WebElement messageContent = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-pnotify-text")));
//            String messageText = messageContent.getText();
//
//            ProcessErrorMessageDTO processErrorMessage = new ProcessErrorMessageDTO(fileName, id, messageText);
//            ProcessErrorMessageField.add(processErrorMessage);
//
//            return messageContent.isDisplayed();
//        } catch (Exception e) {
//            System.out.println("Error while extracting message: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public static List<ProcessErrorMessageDTO> getProcessErrorMessages() {
//        return new ArrayList<>(ProcessErrorMessageField);
//    }
//}
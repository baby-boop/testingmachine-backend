//package testingmachine_backend.process.utils;
//
//import lombok.extern.slf4j.Slf4j;
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
//@Slf4j
//public class trashMessage {
//
//    @ProcessErrorMessageField
//    private static final List<ProcessErrorMessageDTO> ProcessErrorMessageField = new ArrayList<>();
//    private static int successCount = 0;
//    private static int warningCount = 0;
//
//    public static boolean isErrorMessagePresent(WebDriver driver, String id, String fileName) {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
//            WebElement messageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card.light.bg-inverse.mt15")));
//            WebElement messageTitle = messageContainer.findElement(By.cssSelector(".card-body"));
//            WebElement alertElement = messageTitle.findElement(By.cssSelector(".alert"));
//            String messageTitleText = messageTitle.getText().toLowerCase();
//            String alertClassName = alertElement.getAttribute("class");
//
//            if (messageTitleText.contains("error") || alertClassName.contains("alert alert-danger")) {
//                return extractErrorMessage(driver,  id, fileName);
//            }else if (messageTitleText.contains("success") || alertClassName.contains("alert alert-success")){
//                successCount++;
//                log.warn("Success: " + successCount + " - " + id);
//            }else if (messageTitleText.contains("warning") || alertClassName.contains("alert alert-warning")){
//                log.warn("Warning: " + warningCount + " - " + id);
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
//
//            WebElement messageContainer = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card.light.bg-inverse.mt15")));
//            WebElement messageTitle = messageContainer.findElement(By.cssSelector(".card-body"));
//            String messageText = messageTitle.getText();
//            ProcessErrorMessageDTO processErrorMessage = new ProcessErrorMessageDTO(fileName, id, messageText);
//            ProcessErrorMessageField.add(processErrorMessage);
//
//            return messageTitle.isDisplayed();
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

package testingmachine_backend.config;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.Controller.ListConfig;

public class ConfigForAll {

    public static void loginForm(WebDriverWait wait, String username, String password) {
        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
        userNameField.sendKeys(username);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
        passwordField.sendKeys(password);

        WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
        checkBox.click();

        passwordField.sendKeys(Keys.ENTER);
    }

    public static void loginFormTest(WebDriverWait wait) {
        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
        userNameField.sendKeys(JsonController.getUsername());

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
        passwordField.sendKeys(JsonController.getPassword());

        WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
        checkBox.click();

        passwordField.sendKeys(Keys.ENTER);
    }

    private static final String PORT = ":8080";
    private static final String URL = "/erp-services/RestWS/runJson";
    private static final String GOLOMT_URL = "/javarestapi";
    public static final String API_URL = PORT + URL;
    public static final String CALL_PROCESS = "/mdprocess/renderByTestTool/";
    public static final String CALL_DATAVIEW = "/mdobject/dataview/";
}

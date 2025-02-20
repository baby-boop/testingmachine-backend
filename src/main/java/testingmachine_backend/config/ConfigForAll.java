package testingmachine_backend.config;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.Controller.ListConfig;

public class ConfigForAll {

    public static void loginForm(WebDriverWait wait, String username, String password, String isLoginCheckBox) {

        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
        userNameField.sendKeys(username);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
        passwordField.sendKeys(password);

        if (isLoginCheckBox != null && !isLoginCheckBox.isEmpty()) {
            WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
            checkBox.click();
        }

        passwordField.sendKeys(Keys.ENTER);
    }


    private static final String PORT = ":8080";
    private static final String URL = "/erp-services/RestWS/runJson";
    public static final String GOLOMT_URL = "/javarestapi";
    public static final String API_URL = PORT + URL;
    public static final String REST_URL = "/restapi";
    public static final String INDICATOR_URL = PORT + REST_URL;
    public static final String CALL_PROCESS = "/mdprocess/renderByTestTool/";
    public static final String CALL_DATAVIEW = "/mdobject/dataview/";
    public static final String CALL_METAVERSE = "/mdform/indicatorList/";
    public static final String CALL_INDICATOR = "/mdobject/meta/";
}

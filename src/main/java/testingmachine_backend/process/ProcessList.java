package testingmachine_backend.process;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testingmachine_backend.meta.Controller.ProcessMetaData;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.ProcessCallDataview;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.process.Config.ConfigProcess.*;

public class ProcessList {

    private final WebDriver driver;
    private final static int totalProcessCount = 0;

    public ProcessList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(){
        try{

            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);
            driver.get(ConfigProcess.LoginUrl);

            WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
            userNameField.sendKeys(ConfigProcess.USERNAME);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
            passwordField.sendKeys(ConfigProcess.PASSWORD);

            WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
            checkBox.click();


            passwordField.sendKeys(Keys.ENTER);

            List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList();


            int count = 0;
            for (ProcessMetaData metaData : processMetaDataList) {
                String url = ConfigProcess.MainUrl + metaData.getId();
                driver.get(url);

                Thread.sleep(1000);

                waitUtils(driver);

                ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName());
                count++;
                System.out.println("Process count: " + count + ", id: " + metaData.getId());
            }
            System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());

        } catch (Exception e) {
//
        }
    }

    public static int getTotalCount() {
        return totalProcessCount;
    }

//    public void removeCaptcha() {
//        WebElement captchaContainer = driver.findElement(By.cssSelector("div.form-group.row.fom-row"));
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//        jsExecutor.executeScript("arguments[0].remove();", captchaContainer);
//    }



}

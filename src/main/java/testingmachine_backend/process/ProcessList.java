package testingmachine_backend.process;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testingmachine_backend.meta.Controller.ProcessMetaData;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.ProcessCallDataview;
import testingmachine_backend.process.Controller.ProcessController;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.process.Config.ConfigProcess.*;

public class ProcessList {

    private final WebDriver driver;

    public ProcessList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(){
        try{

            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);

            driver.get(ConfigProcess.LoginUrl);

            String databaseName = ProcessController.getDatabaseName();
            if (!databaseName.isEmpty()) {
                System.out.println("Database name is already set: " + databaseName);
                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                Thread.sleep(2000);

                WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
                userNameField.sendKeys(ProcessController.getUsername());

                WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
                passwordField.sendKeys(ProcessController.getPassword());

                WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
                checkBox.click();

                passwordField.sendKeys(Keys.ENTER);

                List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList();
                System.out.println(processMetaDataList.size());

                int count = 0;
                for (ProcessMetaData metaData : processMetaDataList) {
                    String url = ConfigProcess.MainUrl + metaData.getId();
                    driver.get(url);

                    Thread.sleep(1000);

                    waitUtils(driver);

                    ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process");
                    count++;

                    System.out.println("Process count: " + count + ", id: " + metaData.getId());
                }
                System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());
            } else {
                System.out.println("Database name is empty or null: " + databaseName);

                WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
                userNameField.sendKeys(ProcessController.getUsername());

                WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
                passwordField.sendKeys(ProcessController.getPassword());

                WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
                checkBox.click();

                passwordField.sendKeys(Keys.ENTER);

                List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList();
                System.out.println(processMetaDataList.size());

                int count = 0;
                for (ProcessMetaData metaData : processMetaDataList) {
                    String url = ConfigProcess.MainUrl + metaData.getId();
                    driver.get(url);

                    Thread.sleep(1000);

                    waitUtils(driver);

                    ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process");
                    count++;

                    System.out.println("Process count: " + count + ", id: " + metaData.getId());
                }
                System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());
            }


        } catch (Exception e) {
//
        }
    }


}

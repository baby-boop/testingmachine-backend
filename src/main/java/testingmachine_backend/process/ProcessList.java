package testingmachine_backend.process;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.config.CounterService;
import testingmachine_backend.meta.Controller.ProcessMetaData;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.ProcessCallDataview;
import testingmachine_backend.controller.JsonController;
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

            String databaseName = JsonController.getDatabaseName();
            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);
                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                Thread.sleep(2000);

                mainProcess(wait);
            } else {
                System.out.println("Database name is empty or null: " + databaseName);

                mainProcess(wait);
            }


        } catch (Exception e) {
//
        }
    }

    private void mainProcess(WebDriverWait wait) throws InterruptedException {

        ConfigForAll.loginForm(wait);

        List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList();
        int totalMetaCount = processMetaDataList.size();

        int count = 0;
        for (ProcessMetaData metaData : processMetaDataList) {
            String url = ConfigProcess.MainUrl + metaData.getId();
            driver.get(url);

            Thread.sleep(1000);

            waitUtils(driver);

            ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process");
            count++;

            CounterService.addCounter(count, totalMetaCount);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
        System.out.println("End date: " + DateUtils.getCurrentDateTime());
    }

}

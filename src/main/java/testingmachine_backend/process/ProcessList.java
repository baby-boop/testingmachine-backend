package testingmachine_backend.process;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.meta.Controller.ProcessMetaData;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.ProcessCallDataview;
import testingmachine_backend.process.Controller.ProcessCallDataviewWithId;
import testingmachine_backend.process.DTO.ProcessDTO;
import testingmachine_backend.process.Service.ProcessService;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.process.Config.ConfigProcess.*;

public class ProcessList {

    private final WebDriver driver;

    public ProcessList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password, String processId){
        try{

            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);

            driver.get(ConfigProcess.LoginUrl);

            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);
                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                Thread.sleep(2000);

                mainProcessWithModule(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);
            } else {
                if (!processId.isEmpty()) {
                    mainProcessWithModuleWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, processId);
                }else{
                    mainProcessWithModule(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);
                }
            }
        } catch (Exception e) {
//
        }
    }

    private void mainProcessWithModule(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                       String unitName, String systemUrl, String username, String password) throws InterruptedException {

        ConfigForAll.loginForm(wait);

        List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList(moduleId, unitName, systemUrl, username, password);
        int totalMetaCount = processMetaDataList.size();

        int count = 0;
        for (ProcessMetaData metaData : processMetaDataList) {

            String url = ConfigProcess.MainUrl + metaData.getId();

            driver.get(url);

            Thread.sleep(1000);

            waitUtils(driver);

            ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process", jsonId);
            count++;

            ProcessDTO processDTO = new ProcessDTO(theadId, totalMetaCount, count, customerName, createdDate, jsonId, moduleId);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
        System.out.println("End date: " + DateUtils.getCurrentDateTime());
    }


    private void mainProcessWithModuleWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                             String unitName, String systemUrl, String username, String password, String processId) throws InterruptedException {

        ConfigForAll.loginForm(wait);

        List<ProcessMetaData> processMetaDataList = ProcessCallDataviewWithId.getProcessMetaDataList(unitName, systemUrl, username, password, processId);
        int totalMetaCount = processMetaDataList.size();

        int count = 0;

        for (ProcessMetaData metaData : processMetaDataList) {
            String url = ConfigProcess.MainUrl + metaData.getId();
            driver.get(url);

            Thread.sleep(1000);

            waitUtils(driver);

            ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process", jsonId);
            count++;

            ProcessDTO processDTO = new ProcessDTO(theadId, totalMetaCount, count, customerName, createdDate, jsonId, moduleId);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
        System.out.println("End date: " + DateUtils.getCurrentDateTime());

    }

}




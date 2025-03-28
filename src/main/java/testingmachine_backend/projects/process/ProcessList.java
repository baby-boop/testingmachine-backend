package testingmachine_backend.projects.process;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.projects.meta.Controller.ProcessMetaData;
import testingmachine_backend.projects.process.Config.ConfigProcess;
import testingmachine_backend.projects.process.Controller.ProcessCallDataview;
import testingmachine_backend.projects.process.Controller.ProcessCallDataviewWithId;
import testingmachine_backend.projects.process.DTO.ProcessDTO;
import testingmachine_backend.projects.process.Service.ProcessService;
import testingmachine_backend.projects.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.config.ConfigForAll.CALL_PROCESS;
import static testingmachine_backend.projects.process.Config.ConfigProcess.*;

public class ProcessList {

    private final WebDriver driver;

    public ProcessList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) {
        try {

            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);

            String sysURL = systemUrl + "/login";
            driver.get(sysURL);

            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);
                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                Thread.sleep(2000);

                mainProcessWithModule(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, isLoginCheckBox);

            } else {
                if (!processId.isEmpty()) {
                    mainProcessWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, processId, isLoginCheckBox);
                } else {
                    mainProcessWithModule(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, isLoginCheckBox);
                }
            }
        } catch (Exception e) {
//
        }
    }

    private void mainProcessWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) throws InterruptedException {

        ConfigForAll.loginForm(driver, wait, username, password, isLoginCheckBox);

        List<ProcessMetaData> processMetaDataList = ProcessCallDataviewWithId.getProcessMetaDataList(unitName, systemUrl, username, password, processId);

        int count = 0;
        int totalCount = processMetaDataList.size();

        for (ProcessMetaData metaData : processMetaDataList) {
            String url = systemUrl + CALL_PROCESS + metaData.getId();
            driver.get(url);

            Thread.sleep(1000);

            waitUtils(driver);

            ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "nodata", jsonId, totalCount);
            count++;

            ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
        System.out.println("End date: " + DateUtils.getCurrentDateTime());

    }

    private void mainProcessWithModule(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                       String unitName, String systemUrl, String username, String password, String isLoginCheckBox) throws InterruptedException {

        ConfigForAll.loginForm(driver, wait, username, password, isLoginCheckBox);

        List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList(moduleId, unitName, systemUrl, username, password);

        int count = 0;
        int totalCount = processMetaDataList.size();
        for (ProcessMetaData metaData : processMetaDataList) {

            String url = systemUrl + CALL_PROCESS + metaData.getId();

            driver.get(url);

            Thread.sleep(1000);

            waitUtils(driver);

            ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process", jsonId, totalCount);
            count++;

            ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
        ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
        ProcessService.getInstance().updateOrAddProcessResult(processDTO);
        System.out.println("End date: " + DateUtils.getCurrentDateTime());
    }

}
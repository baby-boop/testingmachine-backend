package testingmachine_backend.patch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.meta.Controller.ListConfig;
import testingmachine_backend.meta.Controller.ProcessMetaData;
import testingmachine_backend.meta.Utils.IsErrorMessage;
import testingmachine_backend.meta.Utils.WaitUtils;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.ProcessCallDataview;
import testingmachine_backend.process.Controller.ProcessCallDataviewWithId;
import testingmachine_backend.process.DTO.ProcessDTO;
import testingmachine_backend.process.Service.ProcessService;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.config.ConfigForAll.CALL_DATAVIEW;
import static testingmachine_backend.config.ConfigForAll.CALL_PROCESS;
import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;

public class patchList {

    private final WebDriver driver;

    public patchList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password, String patchId){
        try{

            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);

            String sysURL = systemUrl + "/login";
            driver.get(sysURL);

            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);
                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                Thread.sleep(2000);

                mainProcessWithModuleWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, patchId);
            } else {
                mainProcessWithModuleWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, patchId);
            }
        } catch (Exception e) {
//
        }
    }

    private void mainProcessWithModuleWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                             String unitName, String systemUrl, String username, String password, String patchId) throws InterruptedException {

        ConfigForAll.loginForm(wait, username, password);

        List<PatchDTO> processMetaDataList = PatchCallService.getPatchMetaDataList(unitName, systemUrl, username, password, patchId);

        int count = 0;
        int totalCount = processMetaDataList.size();

        for (PatchDTO metaData : processMetaDataList) {
            if("200101010000011".equals(metaData.getTypeId())){

                String processUrl = systemUrl + CALL_PROCESS + metaData.getId();
                driver.get(processUrl);
                Thread.sleep(1000);

                waitUtils(driver);

                ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getPatchName(), metaData.getCode(), metaData.getName(), "patch", jsonId);
                count++;
            }else if ("200101010000016".equals(metaData.getTypeId())){

                String metaUrl = systemUrl + CALL_DATAVIEW + metaData.getId();
                driver.get(metaUrl);

                WaitUtils.retryWaitForLoadToDisappear(driver, metaData.getPatchName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "patch", 3);
                WaitUtils.retryWaitForLoadingToDisappear(driver, metaData.getPatchName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "patch", 3);

                if (IsErrorMessage.isErrorMessagePresent(driver, metaData.getId(), metaData.getPatchName(), metaData.getCode(), metaData.getName(), jsonId, "patch", totalCount, customerName)) {
                    System.out.println("Error found in ID: " + metaData.getId() );
                }
                count++;
            }

            ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId() + ",  total: " + processMetaDataList.size());
        }
        System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());

    }

}

package testingmachine_backend.projects.patch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.projects.meta.Utils.IsErrorMessage;
import testingmachine_backend.projects.meta.Utils.WaitUtils;
import testingmachine_backend.projects.process.Config.ConfigProcess;
import testingmachine_backend.projects.process.DTO.ProcessDTO;
import testingmachine_backend.projects.process.Service.ProcessService;
import testingmachine_backend.projects.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.config.ConfigForAll.*;
import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;

public class patchList {

    private final WebDriver driver;

    public patchList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password, String patchId, String isLoginCheckBox){
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

                mainProcessWithModuleWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, patchId, isLoginCheckBox);
            } else {
                mainProcessWithModuleWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, patchId, isLoginCheckBox);
            }
        } catch (Exception e) {
//
        }
    }

    private void mainProcessWithModuleWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                             String unitName, String systemUrl, String username, String password, String patchId, String isLoginCheckBox) throws InterruptedException {

        ConfigForAll.loginForm(driver, wait, username, password, isLoginCheckBox);

        List<PatchDTO> processMetaDataList = PatchCallService.getPatchMetaDataList(unitName, systemUrl, username, password, patchId);

        int count = 0;
        int totalCount = processMetaDataList.size();
        System.out.println("Total count: " + totalCount);

        for (PatchDTO metaData : processMetaDataList) {
            if("200101010000011".equals(metaData.getTypeId())){

                String processUrl = systemUrl + CALL_PROCESS + metaData.getId();
                driver.get(processUrl);
                Thread.sleep(1000);

                waitUtils(driver);

                ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getPatchName(), metaData.getCode(), metaData.getName(), "patch", jsonId, totalCount);
                count++;
            }else if ("200101010000016".equals(metaData.getTypeId())){

                String metaUrl = systemUrl + CALL_DATAVIEW + metaData.getId();
                driver.get(metaUrl);

                WaitUtils.retryWaitForLoadToDisappear(driver, metaData.getPatchName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "patch", totalCount, customerName, 3);
                WaitUtils.retryWaitForLoadingToDisappear(driver, metaData.getPatchName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "patch", totalCount, customerName, 3);

                if (IsErrorMessage.isErrorMessagePresent(driver, metaData.getId(), metaData.getPatchName(), metaData.getCode(), metaData.getName(), jsonId, "patch", totalCount, customerName)) {
                    System.out.println("Error found in ID: " + metaData.getId() );
                }
                count++;
            }
//            else if("2008".equals(metaData.getTypeId())){
//                String methodUrl = systemUrl + CALL_METHOD + metaData.getId();
//                driver.get(methodUrl);
//                waitUtils(driver);
//                IndicatorPath.isProcessPersent(driver, metaData.getId(), metaData.getPatchName(), metaData.getCode(), metaData.getName(), jsonId, "patch", totalCount);
//                count++;
//            }

            ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId() + ",  total: " + processMetaDataList.size());
        }
        System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());

    }

}

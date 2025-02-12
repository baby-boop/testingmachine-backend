package testingmachine_backend.indicator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

import java.util.ArrayList;
import java.util.List;

import static testingmachine_backend.config.ConfigForAll.CALL_PROCESS;
import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;

public class IndicatorList {

    private final WebDriver driver;

    public IndicatorList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password, String processId) {
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

                mainProcessWithModule(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);

            } else {
                if (!processId.isEmpty()) {
                    mainProcessWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, processId);
                } else {
                    mainProcessWithModule(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);
                }
            }
        } catch (Exception e) {
//
        }
    }

    private void mainProcessWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String unitName, String systemUrl, String username, String password, String processId) throws InterruptedException {

        try {


            ConfigForAll.loginForm(wait, username, password);

//        List<ProcessMetaData> processMetaDataList = ProcessCallDataviewWithId.getProcessMetaDataList(unitName, systemUrl, username, password, processId);
            List<ProcessMetaData> processMetaDataList = List.of(new ProcessMetaData("1525", "testIndicator", "1", "indicator"));

            int count = 0;

            for (ProcessMetaData metaData : processMetaDataList) {
                String url = "https://dev.veritech.mn/mdobject/dataview/16413658595761?pdfid=17059188884159&pdsid=196119458&mmid=164560279791910";
                driver.get(url);

                Thread.sleep(15000);

                driver.findElement(By.xpath("//div[@class='main-container-1641543359224498']//li[@data-auto-number='5']/a")).click();

                Thread.sleep(2000);
                driver.findElement(By.xpath("//div[@class='main-container-1641543359224498']//a[contains(@class, 'btn-success') and contains(text(), 'Нэмэх')]")).click();

                Thread.sleep(2000);
                waitUtils(driver);

                IndicatorPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "indicator", jsonId);
                count++;

                ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
                ProcessService.getInstance().updateOrAddProcessResult(processDTO);

                System.out.println("Process count: " + count + ", id: " + metaData.getId());
            }
            System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainProcessWithModule(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                       String unitName, String systemUrl, String username, String password) throws InterruptedException {

        ConfigForAll.loginForm(wait, username, password);

        List<ProcessMetaData> processMetaDataList = ProcessCallDataview.getProcessMetaDataList(moduleId, unitName, systemUrl, username, password);

        int count = 0;
        for (ProcessMetaData metaData : processMetaDataList) {

            String url = systemUrl + CALL_PROCESS + metaData.getId();

            driver.get(url);

            Thread.sleep(1000);

            waitUtils(driver);

            ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getSystemName(), metaData.getCode(), metaData.getName(), "process", jsonId);
            count++;

            ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
        ProcessDTO processDTO = new ProcessDTO(theadId, processMetaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
        ProcessService.getInstance().updateOrAddProcessResult(processDTO);
        System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());
    }

}

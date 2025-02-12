package testingmachine_backend.metaverse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.meta.Controller.ListConfig;
import testingmachine_backend.meta.Controller.MetaCallDataview;
import testingmachine_backend.meta.DTO.MetadataDTO;
import testingmachine_backend.meta.Utils.IsErrorMessage;
import testingmachine_backend.meta.Utils.WaitUtils;
import testingmachine_backend.process.DTO.ProcessDTO;
import testingmachine_backend.process.Service.ProcessService;

import java.util.List;

import static testingmachine_backend.config.ConfigForAll.CALL_METAVERSE;

public class MetaverseList {


    private final WebDriver driver;

    public MetaverseList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainList(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password) {
        try {
            WebDriverWait wait = ListConfig.getWebDriverWait(driver);

            String loginUrl = systemUrl + "/login";
            driver.get(loginUrl);

            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);

                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                ConfigForAll.loginForm(wait, username, password);

                ListConfig.selectCompanyFunction(driver, wait, "Хишиг-Арвин Групп");

                workingWithMainList(driver, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);

            }
            else {

                ConfigForAll.loginForm(wait, username, password);

                workingWithMainList(driver, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);

            }


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void workingWithMainList(WebDriver driver,  String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                            String unitName, String systemUrl, String username, String password) {

        List<MetadataDTO> metaDataList = MetaCallDataview.getProcessMetaDataList(moduleId, unitName, systemUrl, username, password);
        int count = 0;
        int errorCount = 0;
        int totalCount = metaDataList.size();

        for (MetadataDTO metaData : metaDataList) {

            String url = systemUrl + CALL_METAVERSE + metaData.getId();

            driver.get(url);

            WaitUtils.retryWaitForLoadToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "metaverse", 3);
            WaitUtils.retryWaitForLoadingToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "metaverse", 3);

            if (IsErrorMessage.isErrorMessagePresent(driver, metaData.getId(), metaData.getModuleName(), metaData.getCode(), metaData.getName(), jsonId, "metaverse", totalCount, customerName)) {
                errorCount++;
                System.out.println("Error found in ID: " + metaData.getId() + ", errorCount: " + errorCount);

            }

            count++;

            ProcessDTO processDTO = new ProcessDTO(theadId, metaDataList.size(), count, customerName, createdDate, jsonId, moduleId, systemUrl);
            ProcessService.getInstance().updateOrAddProcessResult(processDTO);

            System.out.println("Process count: " + count + ", id: " + metaData.getId());

        }
    }
}

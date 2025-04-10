package testingmachine_backend.projects.meta.MetaList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.projects.meta.Controller.ListConfig;
import testingmachine_backend.projects.meta.Controller.MetaCallDataview;
import testingmachine_backend.projects.meta.DTO.MetadataDTO;
import testingmachine_backend.projects.meta.Utils.IsErrorMessage;

import java.util.List;

import testingmachine_backend.projects.meta.Utils.WaitUtils;
import testingmachine_backend.projects.process.DTO.ProcessDTO;
import testingmachine_backend.projects.process.Service.ProcessService;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;

public class MetaLists {

    private final WebDriver driver;

    public MetaLists(WebDriver driver) {
        this.driver = driver;
    }

    public void mainList(String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                         String databaseName, String unitName, String systemUrl, String username, String password, String isLoginCheckBox) {
        try {
            WebDriverWait wait = ListConfig.getWebDriverWait(driver);

            String loginUrl = systemUrl + "/login";
            driver.get(loginUrl);

            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);

                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

            }

            ConfigForAll.loginForm(driver, wait, username, password, isLoginCheckBox);

//                ListConfig.selectCompanyFunction(driver, wait, "Грийн Трейд ХХК");

            workingWithMainList(driver, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password);


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void workingWithMainList(WebDriver driver, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                            String unitName, String systemUrl, String username, String password) {

        List<MetadataDTO> metaDataList = MetaCallDataview.getProcessMetaDataList(moduleId, unitName, systemUrl, username, password);
        int count = 0;
        int errorCount = 0;
        int totalCount = metaDataList.size();
        System.out.println("Total count is: " + totalCount);

        for (MetadataDTO metaData : metaDataList) {

            String url = systemUrl + ListConfig.MainUrl + metaData.getId();

            driver.get(url);

//            WaitUtils.retryWaitForLoadToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "meta", totalCount, customerName, 3);
//            WaitUtils.retryWaitForLoadingToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, "meta",totalCount, customerName, 3);
            waitUtils(driver);

            if (IsErrorMessage.isErrorMessagePresent(driver, metaData.getId(), metaData.getModuleName(), metaData.getCode(), metaData.getName(), jsonId, "meta", totalCount, customerName)) {
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

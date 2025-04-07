package testingmachine_backend.projects.trail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.projects.meta.Controller.MetaCallDataview;
import testingmachine_backend.projects.meta.DTO.MetadataDTO;
import testingmachine_backend.projects.meta.Utils.IsErrorMessage;
import testingmachine_backend.projects.meta.Utils.WaitUtils;
import testingmachine_backend.projects.patch.PatchCallService;
import testingmachine_backend.projects.patch.PatchDTO;
import testingmachine_backend.projects.process.Config.ConfigProcess;
import testingmachine_backend.projects.process.DTO.ProcessDTO;
import testingmachine_backend.projects.process.Service.ProcessService;
import testingmachine_backend.projects.process.utils.ProcessPath;
import testingmachine_backend.projects.product.CallModuleIndicatorList;
import testingmachine_backend.projects.product.ProductTest;

import java.util.List;

import static testingmachine_backend.config.ConfigForAll.*;
import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;

public class TrailList {

    private final WebDriver driver;

    public TrailList(WebDriver driver) {
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
            }
            ConfigForAll.loginForm(driver, wait, username, password, isLoginCheckBox);

            selectDbSelection(driver, wait);

            waitUtils(driver);

            mainProcessWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, processId, isLoginCheckBox);

        } catch (Exception e) {

        }
    }

    private void selectDbSelection(WebDriver driver, WebDriverWait wait) {
        try{
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='kt_app_engage']")));

            Actions action = new Actions(driver);
            action.moveToElement(MainProcess).perform();

            WebElement closeButton = MainProcess.findElement(
                    By.cssSelector("a[onclick='connectCloudUserDatabase(this);']")
            );
            closeButton.click();

            Thread.sleep(2000);

            WebElement MainConnect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='cloud-user-db-connections']")));
            WebElement clickConnect = MainConnect.findElement(
                    By.cssSelector("a[data-id='1729663344974444']")
            );
            clickConnect.click();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void mainProcessWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) throws InterruptedException {

        try {

            List<MetadataDTO> metaDataList = CallModuleIndicatorList.getIndicatorList(moduleId, username, password);
            int totalCount = metaDataList.size();
            System.out.println(totalCount);
            for (MetadataDTO metaData : metaDataList) {
                String url = systemUrl +  CALL_PRODUCT + metaData.getId();
                driver.get(url);

                Thread.sleep(2000);
                waitUtils(driver);

                ProductTest.findAndWorkingSiderTabsTest(driver, customerName, metaData.getId(), "", "", "trail", jsonId, totalCount);

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

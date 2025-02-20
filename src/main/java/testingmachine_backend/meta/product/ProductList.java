package testingmachine_backend.meta.product;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.indicator.IndicatorPath;
import testingmachine_backend.meta.Controller.ListConfig;
import testingmachine_backend.meta.Controller.MetaCallDataview;
import testingmachine_backend.meta.Controller.ProcessMetaData;
import testingmachine_backend.meta.DTO.MetadataDTO;
import testingmachine_backend.meta.Utils.IsErrorMessage;
import testingmachine_backend.meta.Utils.WaitUtils;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.Controller.ProcessCallDataview;
import testingmachine_backend.process.DTO.ProcessDTO;
import testingmachine_backend.process.Service.ProcessService;
import testingmachine_backend.process.utils.ProcessPath;

import java.util.List;

import static testingmachine_backend.config.ConfigForAll.CALL_METAVERSE;
import static testingmachine_backend.config.ConfigForAll.CALL_PROCESS;
import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;

public class ProductList {

    private final WebDriver driver;

    public ProductList(WebDriver driver) {
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

                if (!processId.isEmpty()) {
                    mainProcessWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, processId, isLoginCheckBox);
                }

            } else {
                if (!processId.isEmpty()) {
                    mainProcessWithId(wait, jsonId, theadId, customerName, createdDate, moduleId, unitName, systemUrl, username, password, processId, isLoginCheckBox);
                }
            }
        } catch (Exception e) {

        }
    }

    private void mainProcessWithId(WebDriverWait wait, String jsonId, String theadId, String customerName, String createdDate, String moduleId,
                                   String unitName, String systemUrl, String username, String password, String processId, String isLoginCheckBox) throws InterruptedException {

        try {

            ConfigForAll.loginForm(wait, username, password, isLoginCheckBox);

                String url = "https://dev.veritech.mn/mdobject/dataview/16413658595761?pdfid=16783426616019&pdsid=17176678746843&mmid=164560279791910"; /* seek */
                driver.get(url);

                Thread.sleep(2000);
                waitUtils(driver);

                IndicatorPath.isProcessPersent(driver, processId, customerName, "", "", "product", jsonId);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

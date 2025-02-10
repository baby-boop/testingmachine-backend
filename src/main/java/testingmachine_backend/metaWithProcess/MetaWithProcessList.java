package testingmachine_backend.metaWithProcess;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.meta.DTO.MetadataDTO;
import testingmachine_backend.metaWithProcess.Controller.CallMetaWithProcess;
import testingmachine_backend.metaWithProcess.Controller.Config;
import testingmachine_backend.metaWithProcess.Controller.IsMetaErrorList;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.process.utils.ProcessPath;

import java.time.Duration;
import java.util.List;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;


public class MetaWithProcessList {
    private final WebDriver driver;
    private static final int SHORT_WAIT_SECONDS = 2;

    public MetaWithProcessList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(String jsonId) {
        try {
            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);

            driver.get(ConfigProcess.LoginUrl);

            String databaseName = JsonController.getDatabaseName();
            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);

                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                Thread.sleep(2000);
                }
            else{
//                ConfigForAll.loginForm(wait);
                mainList(wait, driver, jsonId);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void firstRowFromMeta(WebDriver driver, String id, String processName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(@id,'datagrid-row-r')]")));
            List<WebElement> rows = driver.findElements(By.xpath("//tr[contains(@id,'datagrid-row-r')]"));

            if (!rows.isEmpty()) {

                WebElement firstRow = rows.get(0);
                WebElement firstCell = firstRow.findElement(By.xpath(".//td[1]"));
                if (firstCell != null) {
                    scrollToElement(driver, firstCell);
                    rows.clear();
                    waitUtils(driver);
                    WebElement editButton = driver.findElement(By.xpath("//div[@data-process-id='" + id + "']//a[text()='"+ processName +"']"));
                    if(editButton != null) {
                        editButton.click();
                    }else {
                        System.out.println("Edit button is null");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public static void mainList(WebDriverWait wait, WebDriver driver, String jsonId) {
        try{
//            ConfigForAll.loginFormTest(wait);

            List<MetadataDTO> metaWithProcessList = CallMetaWithProcess.getProcessMetaDataList();
            System.out.println(metaWithProcessList.size());

            int count = 0;
            for (MetadataDTO metaData : metaWithProcessList) {
                String url = Config.MainUrl + metaData.getId();
                driver.get(url);

                Thread.sleep(1000);

                waitUtils(driver);

                if (IsMetaErrorList.isErrorMessagePresent(driver, metaData.getId(), metaData.getModuleName(), metaData.getCode(), metaData.getName(), jsonId)) {
                    System.out.println("Error with process found in ID: " + metaData.getId());
                    waitUtils(driver);
                }else{
                    firstRowFromMeta(driver, metaData.getId(), metaData.getModuleName());
                    waitUtils(driver);
                    Thread.sleep(5000);
                    ProcessPath.isProcessPersent(driver, metaData.getId(), metaData.getModuleName(), metaData.getCode(), metaData.getName(), "meta", jsonId);
                }
                count++;

                System.out.println("Process count: " + count + ", id: " + metaData.getId());
            }
            System.out.println("End date: " + ConfigProcess.DateUtils.getCurrentDateTime());
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }
}

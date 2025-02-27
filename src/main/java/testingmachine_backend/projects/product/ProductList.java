package testingmachine_backend.projects.product;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.projects.process.Config.ConfigProcess;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;

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

                ConfigForAll.loginForm(driver, wait, username, password, isLoginCheckBox);

                String url = "https://dev.veritech.mn/appmenu/mvmodule/" + processId; /* Гэрээ */
                driver.get(url);

                Thread.sleep(2000);
                waitUtils(driver);

                ProductTest.findAndWorkingSiderTabsTest(driver, customerName, processId, "", "", "product", jsonId);



        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

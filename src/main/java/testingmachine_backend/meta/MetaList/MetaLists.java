package testingmachine_backend.meta.MetaList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.meta.Controller.ListConfig;
import testingmachine_backend.meta.Controller.MeteCallDataview;
import testingmachine_backend.meta.DTO.MetadataDTO;
import testingmachine_backend.meta.Utils.IsErrorList;

import java.util.List;

import testingmachine_backend.meta.Utils.WaitUtils;

public class MetaLists {

    private final WebDriver driver;
    private static int metaCount = 0;
    private static int totalMetaCount = 0;


    public MetaLists(WebDriver driver) {
        this.driver = driver;
    }

    public void mainList() {
        try {
            WebDriverWait wait = ListConfig.getWebDriverWait(driver);
            driver.get(ListConfig.LoginUrl);

            Thread.sleep(500);

            ListConfig.selectDbFuntion(driver, wait, "dXgxZERkUjNzSkFhZVc1aUU2dTBNQT09");

            WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
            userNameField.sendKeys(ListConfig.USERNAME);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
            passwordField.sendKeys(ListConfig.PASSWORD);
            passwordField.sendKeys(Keys.ENTER);

            Thread.sleep(3000);
            ListConfig.selectCompanyFunction(driver, wait, "Хишиг-Арвин Групп");

            Thread.sleep(2000);

            List<MetadataDTO> metaDataList = MeteCallDataview.getProcessMetaDataList();
            System.out.println(metaDataList.size());

            int count = 0;
            for (MetadataDTO metaData : metaDataList) {
                String url = ListConfig.MainUrl + metaData.getId();
                driver.get(url);

                Thread.sleep(1000);

                WaitUtils.retryWaitForLoadToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), 3);
                WaitUtils.retryWaitForLoadingToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(),3);

                if (IsErrorList.isErrorMessagePresent(driver, metaData.getId(), metaData.getModuleName(), metaData.getCode(), metaData.getName())) {
                    System.out.println("Error found in ID: " + metaData.getId());
                }
                count++;
                System.out.println("Process count: " + count + ", id: " + metaData.getId());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static int getCheckCount() {
        return metaCount;
    }

    public static int getTotalCount() {
        return totalMetaCount;
    }

}

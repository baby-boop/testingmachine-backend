package testingmachine_backend.process;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.meta.Utils.WaitUtils;
import testingmachine_backend.process.Config.ConfigProcess;
import testingmachine_backend.process.utils.ProcessPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static testingmachine_backend.meta.Utils.FileUtils.readIdsFromFile;

public class ProcessList {

    private final WebDriver driver;
    private static int processCount = 0;
    private static int totalProcessCount = 0;

    public ProcessList(WebDriver driver) {
        this.driver = driver;
    }

    public void mainTool(){
        try{
            Actions action = new Actions(driver);
            WebDriverWait wait = ConfigProcess.getWebDriverWait(driver);
            driver.get(ConfigProcess.LoginUrl);

            WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
            userNameField.sendKeys(ConfigProcess.USERNAME);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
            passwordField.sendKeys(ConfigProcess.PASSWORD);
            passwordField.sendKeys(Keys.ENTER);

            Thread.sleep(2000);


            String url = "https://dev.veritech.mn/mdmetadata/system#objectType=folder&objectId=1728368545166706";


            String directoryPath = "C:\\Users\\batde\\Downloads\\Process";

            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

            if (listOfFiles != null) {

                List<String> allIds = new ArrayList<>();

                for (File file : listOfFiles) {
                    List<String> idsFromFile = readIdsFromFile(file.getAbsolutePath());
                    allIds.addAll(idsFromFile);
                }

                int totalIds = allIds.size();
                totalProcessCount = totalIds;
                System.out.println("Total IDs to meta: " + totalIds);

                for (File file : listOfFiles) {
                    System.out.println("Processing file: " + file.getName());

                    List<String> ids = readIdsFromFile(file.getAbsolutePath());

                    for (String id : ids) {

//                        String url = ListConfig.MainUrl + id;
                        driver.get(url);
                        driver.navigate().refresh();

                        Thread.sleep(1000);

                        WebElement AddProcessButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li[data-id='" + id + "']")));
                        action.doubleClick(AddProcessButton).perform();

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
                        WaitUtils.retryWaitForLoadToDisappear(driver, file.getName(), id, 3);
                        WaitUtils.retryWaitForLoadingToDisappear(driver, file.getName(), id, 3);

                        ProcessPath.isProcessPersent(driver, id, file.getName());
                        processCount++;
                        System.out.println("Count: " + processCount + ", ID: " + id);
                    }
                }
            }
        } catch (Exception e) {
            //
        }
    }
    public static int getCheckCount() {
        return processCount;
    }

    public static int getTotalCount() {
        return totalProcessCount;
    }
}

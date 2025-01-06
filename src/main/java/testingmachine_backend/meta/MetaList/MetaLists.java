package testingmachine_backend.meta.MetaList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.config.ConfigForAll;
import testingmachine_backend.config.CounterService;
import testingmachine_backend.meta.Controller.ListConfig;
import testingmachine_backend.meta.Controller.MetaCallDataview;
import testingmachine_backend.meta.DTO.MetadataDTO;
import testingmachine_backend.meta.Fields.FindBeforeUsedIds;
import testingmachine_backend.meta.Fields.FindUserIdsDTO;
import testingmachine_backend.meta.Utils.IsErrorMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import testingmachine_backend.meta.Utils.WaitUtils;
import testingmachine_backend.controller.JsonController;

import static testingmachine_backend.meta.Utils.FileUtils.readDataFromExcel;

public class MetaLists {

    private final WebDriver driver;

    @FindBeforeUsedIds
    public static final List<FindUserIdsDTO> findBeforeUsedId = new ArrayList<>();

    public MetaLists(WebDriver driver) {
        this.driver = driver;
    }

    public void mainList(String jsonId) {
        try {
            WebDriverWait wait = ListConfig.getWebDriverWait(driver);

            driver.get(ListConfig.LoginUrl);

            String databaseName = JsonController.getDatabaseName();

            if (!databaseName.isEmpty()) {

                System.out.println("Database name is already set: " + databaseName);

                WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
                Select dbSelect = new Select(selectDb);
                dbSelect.selectByVisibleText(databaseName);

                ConfigForAll.loginForm(wait);

                ListConfig.selectCompanyFunction(driver, wait, "Хишиг-Арвин Групп");

                workingWithMainList(driver, jsonId);

            }
            else {

                ConfigForAll.loginForm(wait);

                workingWithMainList(driver, jsonId);

            }


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
//

    private static void workingWithMainList(WebDriver driver, String jsonId) {

        List<MetadataDTO> metaDataList = MetaCallDataview.getProcessMetaDataList();
        int totalMetaCount = metaDataList.size();

        int count = 0;
        int errorCount = 0;

        for (MetadataDTO metaData : metaDataList) {

            String url = ListConfig.MainUrl + metaData.getId();

            driver.get(url);

            WaitUtils.retryWaitForLoadToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, 3);
            WaitUtils.retryWaitForLoadingToDisappear(driver, metaData.getModuleName(), metaData.getId(), metaData.getCode(), metaData.getName(), jsonId, 3);

            if (IsErrorMessage.isErrorMessagePresent(driver, metaData.getId(), metaData.getModuleName(), metaData.getCode(), metaData.getName(), jsonId)) {
                errorCount++;
                System.out.println("Error found in ID: " + metaData.getId() + ", errorCount: " + errorCount);

            }

            count++;
            CounterService.addCounter(count, totalMetaCount);
            System.out.println("Process count: " + count + ", id: " + metaData.getId());
        }
    }

    private static void workingWithExcel(WebDriver driver, WebDriverWait wait, String jsonId) {
        try {
            String directoryPath = "C:\\Users\\batde\\Downloads\\golomt excel";
            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));

            int metaCount = 0;
            int errorCount = 0;

            List<Map<String, String>> allIds = new ArrayList<>();

            assert listOfFiles != null;
            for (File file : listOfFiles) {
                List<Map<String, String>> dataFromExcel = readDataFromExcel(file.getAbsolutePath());
                allIds.addAll(dataFromExcel);
            }

            int totalIds = allIds.size();
            System.out.println("Metadata length: " + totalIds);

            for (File file : listOfFiles) {
                System.out.println("Processing file: " + file.getName());
                List<Map<String, String>> dataFromExcel = readDataFromExcel(file.getAbsolutePath());

                for (Map<String, String> record : dataFromExcel) {
                    String id = record.get("id");
                    String code = record.get("code");
                    String name = record.get("name");
                    String moduleName = record.get("moduleName");

                    if(!isDuplicateIdEntry(id)){
                        String url = ListConfig.MainUrl + id;
                        driver.get(url);
                        driver.navigate().refresh();

                        FindUserIdsDTO userId = new FindUserIdsDTO(id);
                        findBeforeUsedId.add(userId);

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

                        WaitUtils.retryWaitForLoadToDisappear(driver, moduleName, id, code, name, jsonId, 3);
                        WaitUtils.retryWaitForLoadingToDisappear(driver, moduleName, id, code, name, jsonId, 3);

                        if (IsErrorMessage.isErrorMessagePresent(driver, id, moduleName, code, name, jsonId)) {
                            errorCount++;
                            System.out.println("Error found in ID: " + id + "    Module name: " + moduleName + "    Meta error count: " + errorCount);
                        }

                        metaCount++;
                        System.out.println("Meta count: " + metaCount + ", id: " + id);
                    }
                }
            }
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static boolean isDuplicateIdEntry(String id) {
        return MetaLists.findBeforeUsedId.stream()
                .anyMatch(log -> log.getId().equals(id));
    }
}


//Надад маш олон Id байгаа. гэхдээ давхардсан id маш олон тиймээс нэг удаа дуудаад дараагын удаа skip хийдэг больё
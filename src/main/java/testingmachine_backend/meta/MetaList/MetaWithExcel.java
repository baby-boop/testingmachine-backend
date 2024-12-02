//package testingmachine_backend.meta.MetaList;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import testingmachine_backend.meta.Controller.ListConfig;
//import testingmachine_backend.meta.DTO.ErrorTimeoutDTO;
//import testingmachine_backend.meta.Fields.ErrorTimeoutField;
//import testingmachine_backend.meta.Utils.ErrorLogger;
//import testingmachine_backend.meta.Utils.IsErrorList;
//import testingmachine_backend.meta.Utils.WaitUtils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import static testingmachine_backend.meta.Utils.FileUtils.readIdsFromExcel;
//
//
//public class MetaWithExcel {
//
//    private final WebDriver driver;
//    private static int metaCount = 0;
//    private static int totaMetaCount = 0;
//
//    @ErrorTimeoutField
//    private final static List<ErrorTimeoutDTO> errorTimeoutMessages = new ArrayList<>();
//
//    public MetaWithExcel(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    public void mainList() {
//        try {
//            WebDriverWait wait = ListConfig.getWebDriverWait(driver);
//            driver.get(ListConfig.LoginUrl);
//
//            Thread.sleep(500);
//
////            ListConfig.selectDbFuntion( wait, "Z2dSTHRyUWw2STBOcis4T0Z4bzAwQT09");
//
//            WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
//            userNameField.sendKeys(ListConfig.USERNAME);
//
//            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
//            passwordField.sendKeys(ListConfig.PASSWORD);
//            passwordField.sendKeys(Keys.ENTER);
//
//            Thread.sleep(2000);
//
//            String directoryPath = "C:\\Users\\batde\\Downloads\\summit ids";
//
//            File folder = new File(directoryPath);
//            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));
//
//            if (listOfFiles != null) {
//
//                List<String> allIds = new ArrayList<>();
//
//                for (File file : listOfFiles) {
//                    List<String> idsFromFile = readIdsFromExcel(file.getAbsolutePath());
//                    allIds.addAll(idsFromFile);
//                }
//
//                int totalIds = allIds.size();
//                totaMetaCount = totalIds;
//                System.out.println("Total IDs to meta: " + totalIds);
//
//                for (File file : listOfFiles) {
//                    System.out.println("Processing file: " + file.getName());
//                    List<String> idsFromExcel = readIdsFromExcel(file.getAbsolutePath());
//
//                    for (String id : idsFromExcel) {
//                        String url = ListConfig.MainUrl + id;
//                        driver.get(url);
//                        driver.navigate().refresh();
//
//                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
//
//                        WaitUtils.retryWaitForLoadToDisappear(driver, file.getName(), id,3);
//                        WaitUtils.retryWaitForLoadingToDisappear(driver, file.getName(), id,3 );
//
//                        if (IsErrorList.isErrorMessagePresent(driver, id, file.getName())) {
//                            System.out.println("Error found in ID: " + id);
//                        }
//                        metaCount++;
//                        System.out.println("Count: " + metaCount + ", ID: " + id);
//                    }
//                }
//            } else {
//                System.err.println("No Excel files found in directory: " + directoryPath);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//    }
//
//    public static List<ErrorTimeoutDTO> errorTimeoutMessages() {
//        return ErrorLogger.getErrorTimeoutMessages();
//    }
//
//    public static int getCheckCount() {
//        return metaCount;
//    }
//
//    public static int getTotalCount() {
//        return totaMetaCount;
//    }
//}

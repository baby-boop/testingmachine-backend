package testingmachine_backend.metaverse.Main;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.metaverse.Controller.MVconfig;
import testingmachine_backend.metaverse.DTO.MVErrorTimeoutDTO;
import testingmachine_backend.metaverse.Fields.MVErrorTimeoutField;
import testingmachine_backend.metaverse.Utils.MVErrorLogger;
import testingmachine_backend.metaverse.Utils.isErrorMv;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static testingmachine_backend.meta.Utils.FileUtils.readIdMenuPairsFromFile;

public class MVLists {
    private final WebDriver driver;
    private static int inticatorCount = 0;
    private static int totalInticatorCount = 0;

    @MVErrorTimeoutField
    private final static List<MVErrorTimeoutField> MVErrorTimeoutMessages = new ArrayList<>();


    public MVLists(WebDriver driver) {
        this.driver = driver;
    }

    public void mainList() {
        try {
            WebDriverWait wait = MVconfig.getWebDriverWait(driver);

            String directoryPath = "C:\\Users\\batde\\Downloads\\test";

            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

            if (listOfFiles != null) {

                List<String[]> allIdMenuPairs = new ArrayList<>();

                for (File file : listOfFiles) {
                    List<String[]> idMenuPairsFromFile = readIdMenuPairsFromFile(file.getAbsolutePath());
                    allIdMenuPairs.addAll(idMenuPairsFromFile);
                }

                int totalIds = allIdMenuPairs.size();
                totalInticatorCount = totalIds;
                System.out.println("Total IDs in folder: " + totalIds);

                for (File file : listOfFiles) {
                    System.out.println("Processing file: " + file.getName());

                    List<String[]> idMenuPairs = readIdMenuPairsFromFile(file.getAbsolutePath());

                    for (String[] pair : idMenuPairs) {
                        String id = pair[0];
                        String menuId = pair[1];

//                        String url = MVconfig.MainUrl + "pdfid=" + id + "&pdsid=" + menuId + MVconfig.tailUrl;

                        String url = MVconfig.BaseUrl + MVconfig.MetaverseURL + id;

                        driver.get(url);
                        driver.navigate().refresh();

                        System.out.println(url);

                        retryWaitForLoadingToDisappear(driver, file.getName(), id,menuId);
                        retryWaitForLoadToDisappear(driver, file.getName(), id,menuId);


                        if (isErrorMv.isErrorMessagePresent(driver, id, menuId, file.getName())) {
                            System.out.println("Error found in ID: " + id);
                        }

                        inticatorCount++;
                        System.out.println("Count: " + inticatorCount + ", ID: " + id + ", Menu ID: " + menuId);
                    }
                }
            } else {
                System.err.println("No .txt files found in directory: " + directoryPath);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }



    public static List<MVErrorTimeoutDTO> MVErrorTimeoutMessages() {
        return MVErrorLogger.getMvErrorTimeoutMessages();
    }

    public static int getCheckMvCount() {
        return inticatorCount;
    }

    public static int getMvTotalCount() {
        return totalInticatorCount;
    }


    private void retryWaitForLoadingToDisappear(WebDriver driver, String fileName, String id, String menuId) {
        retryAction(() -> waitForLoadingToDisappear(driver, fileName, id,menuId), 3);
    }

    private void retryWaitForLoadToDisappear(WebDriver driver, String fileName, String id, String menuId) {
        retryAction(() -> waitForLoadToDisappear(driver, fileName, id,menuId), 3);
    }

    private static void waitForLoadingToDisappear(WebDriver driver, String fileName, String id, String menuId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            WebElement loadingElement = driver.findElement(By.id("j1_loading"));
            if (loadingElement.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("j1_loading")));
            }
        } catch (NoSuchElementException e) {
            // Element not found, proceed
        } catch (TimeoutException e) {
            MVErrorLogger.logError(fileName, id,menuId);
        }
    }

    private static void waitForLoadToDisappear(WebDriver driver, String fileName, String id, String menuId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            WebElement loadingSpinner = driver.findElement(By.xpath("//div[contains(@class, 'loading-message')]"));

//            WebElement loadingElement = driver.findElement(By.xpath("//div[contains(@class, 'blockUI blockMsg blockPage')]"));
            if (loadingSpinner.isDisplayed()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading-message')]")));
            }
        } catch (NoSuchElementException e) {
            // Loading spinner not found, proceed
        } catch (TimeoutException e) {
            MVErrorLogger.logError(fileName, id,menuId);
        }
    }

    // Retry mechanism for an action
    private void retryAction(Runnable action, int maxAttempts) {
        int attempt = 0;
        while (attempt < maxAttempts) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                attempt++;
                System.out.println("Retrying action (attempt " + attempt + ")");
            }
        }
    }
}



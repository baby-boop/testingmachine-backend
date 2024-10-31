package testingmachine_backend.process.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ProcessPath.LOGGER;
import static testingmachine_backend.process.utils.ProcessPath.*;


public class DetailsFieldUtils {
    public static final int SHORT_WAIT_SECONDS = 2;

    public static void detailActionButton(WebDriver driver, String id) {
        try {
            List<WebElement> elementsWithDataSectionPath = findRowElementsWithDataSectionPath(driver);
            if(elementsWithDataSectionPath != null) {
                for (WebElement element : elementsWithDataSectionPath) {
                    String sectionPath = element.getAttribute("data-section-path");
                    waitUtils(driver);
                    List<WebElement> allActionPath = findRowActionPathsButton(driver, sectionPath);
                    if (allActionPath != null) {
                        waitUtils(driver);
                        for (WebElement action : allActionPath) {
                            String onclick = action.getAttribute("onclick");

                            if (onclick.contains("bpAddMainMultiRow")) {
                                action.click();
                                waitUtils(driver);
                                clickFirstRow(driver, id);
                                waitUtils(driver);
                                break;
                            } else if (onclick.contains("bpAddMainRow")) {
                                waitUtils(driver);
                                action.click();
                                waitUtils(driver);
                                Thread.sleep(1000);
                                List<WebElement> rowElements = findElementsWithDetailsPath(driver, sectionPath);
                                processTabElements(driver, rowElements, id);
                                break;
                            }
                        }
                    }else{
                        List<WebElement> rowElements = findElementsWithDetailsPath(driver, sectionPath);
                        processTabElements(driver, rowElements, id);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in rows: " + id, e);
        }
    }

    public static List<WebElement> findRowElementsWithDataSectionPath(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bp-detail-body .row[data-section-path]")));
            return driver.findElements(By.cssSelector(".bp-detail-body .row[data-section-path]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Elements 'data-section-path' not found");
            return List.of();
        }
    }

    public static List<WebElement> findRowActionPathsButton(WebDriver driver, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            String selector = String.format("button[data-action-path='%s']", sectionPath);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
            return driver.findElements(By.cssSelector(selector));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths: " + sectionPath);
            return null;
        }
    }

    public static List<WebElement> findElementsWithDetailsPath(WebDriver driver, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));

        try {
            WebElement sectionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='bp-detail-body']//div[@data-section-path='" + sectionPath + "']")
            ));

            List<WebElement> elements = sectionElement.findElements(By.cssSelector("[data-path]"));

            Map<String, WebElement> uniqueElements = new LinkedHashMap<>();
            for (WebElement element : elements) {
                String dataPath = element.getAttribute("data-path");
                if (dataPath != null && !uniqueElements.containsKey(dataPath)) {
                    uniqueElements.put(dataPath, element);
                }
            }

            return new ArrayList<>(uniqueElements.values());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with sectionPath '" + sectionPath + "' selector not found", e);
            return Collections.emptyList();
        }
    }

}

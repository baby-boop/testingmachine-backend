package testingmachine_backend.process.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;


public class DetailsFieldUtils {
    public static final int SHORT_WAIT_SECONDS = 2;
    static final Logger LOGGER = Logger.getLogger(DetailsFieldUtils.class.getName());
    public static void detailActionButton(WebDriver driver, String id, String fileName) {
        try {
            List<WebElement> elementsWithDataSectionPath = findRowElementsWithDataSectionPath(driver);
            System.out.println(elementsWithDataSectionPath.size());
            if(elementsWithDataSectionPath != null) {
                for (WebElement element : elementsWithDataSectionPath) {
                    String sectionPath = element.getAttribute("data-section-path");
                    waitUtils(driver);
                    List<WebElement> rowElements = findElementsWithDetailsPath(driver, sectionPath);
                    processTabElements(driver, rowElements, id, fileName);
                    List<WebElement> allActionPath = findRowActionPathsButton(driver, sectionPath);
                    if (allActionPath != null) {
                        List<WebElement> findLayoutDefaultRows = findDefaultRow(driver, sectionPath);
                        assert findLayoutDefaultRows != null;
                        if (findLayoutDefaultRows.isEmpty()) {
                            waitUtils(driver);
                            for (WebElement action : allActionPath) {
                                String onclick = action.getAttribute("onclick");
                                String sectionCode = action.getAttribute("data-section-code");
                                if (onclick.contains("bpAddMainMultiRow")) {
                                    action.click();
                                    waitUtils(driver);
                                    clickFirstRow(driver, id, fileName, sectionPath, "");
                                    waitUtils(driver);
                                    List<WebElement> rowElements3 = findElementsWithDetailsPath(driver, sectionPath);
                                    processTabElements(driver, rowElements3, id, fileName);
                                    break;
                                } else if (onclick.contains("bpAddMainRow")) {
                                    waitUtils(driver);
                                    action.click();
                                    waitUtils(driver);
                                    Thread.sleep(1000);
                                    List<WebElement> rowElements1 = findElementsWithDetailsPath(driver, sectionPath);
                                    processTabElements(driver, rowElements1, id, fileName);
                                    break;
                                }
                            }
                        }
                    }else{
                        List<WebElement> rowElements2 = findElementsWithDetailsPath(driver, sectionPath);
                        processTabElements(driver, rowElements2, id, fileName);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in rows: " + id);
        }
    }

    public static List<WebElement> findRowElementsWithDataSectionPath(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bp-detail-body .row[data-section-path]")));
            return driver.findElements(By.cssSelector(".bp-detail-body .row[data-section-path]:not([style*='display: none'])"));
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

    public static List<WebElement> findDefaultRow(WebDriver driver, String sectionPath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-path='"+ sectionPath +"']")));
            return driver.findElements(By.cssSelector("div[data-section-path='"+ sectionPath +"'] .tbody .bp-detail-row"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths not found: " + sectionPath);
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

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Elements with sectionPath '" + sectionPath + "' not found");
            return Collections.emptyList();
        }
    }

}

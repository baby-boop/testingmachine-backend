package testingmachine_backend.process.Section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ElementsFunctionUtils.*;


public class LayoutProcessSection {
    private static final Logger LOGGER = Logger.getLogger(LayoutProcessSection.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;

    public static void LayoutFieldFunction(WebDriver driver, String id, String fileName) {
        List<WebElement> layoutSectionPaths = findLayoutSectionPath(driver, id);
        if (layoutSectionPaths != null) {
            for (WebElement sectionPath : layoutSectionPaths) {
                String dataSectionCode = sectionPath.getAttribute("data-section-code");
                waitUtils(driver);
                List<WebElement> layoutPath = findLayoutFieldPath(driver, dataSectionCode);
                processTabElements(driver, layoutPath, id, fileName);
                List<WebElement> allActionTabPath = findLayoutActionButton(driver, dataSectionCode);
                if (allActionTabPath != null) {
                    List<WebElement> findLayoutDefaultRows = findLayoutDefaultRow(driver, dataSectionCode);
                    assert findLayoutDefaultRows != null;
                    if (findLayoutDefaultRows.isEmpty()) {
                        waitUtils(driver);
                        for (WebElement action : allActionTabPath) {
                            String onclick = action.getAttribute("onclick");
                            String sectionCode = action.getAttribute("data-section-code");
                            if (onclick.contains("bpAddMainMultiRow")) {
                                action.click();
                                waitUtils(driver);
                                clickFirstRow(driver,  id, fileName, dataSectionCode, "");
                                waitUtils(driver);
                                List<WebElement> layoutDtlPath2 = findLayoutDtlFieldPath(driver, dataSectionCode);
                                processTabElements(driver, layoutDtlPath2, id, fileName);
                                break;
                            } else if (onclick.contains("bpAddMainRow")) {
                                waitUtils(driver);
                                action.click();
                                waitUtils(driver);
                                List<WebElement> layoutDtlPath = findLayoutDtlFieldPath(driver, dataSectionCode);
                                processTabElements(driver, layoutDtlPath, id, fileName);
                                break;
                            }
                        }
                    }else{
                        List<WebElement> layoutPath1 = findLayoutFieldPath(driver, dataSectionCode);
                        processTabElements(driver, layoutPath1, id, fileName);
                    }
                }
            }
        }
    }

    public static List<WebElement> findLayoutSectionPath(WebDriver driver, String processId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            Thread.sleep(2000);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + processId + "']")));

            return driver.findElements(By.xpath(
//                    "//div[@id='bp-window-" + processId + "']//div[@data-section-code and *]"
                    "//div[@data-section-code and *]"

            ));


        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "layout 'data-section-code' not found: " + processId, e);
            return Collections.emptyList();
        }
    }



    public static List<WebElement> findLayoutActionButton(WebDriver driver, String sectionCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-code='"+ sectionCode +"']")));
            return driver.findElements(By.cssSelector("div[data-section-code='"+ sectionCode +"'] button[data-action-path]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths not found: " + sectionCode);
            return null;
        }
    }

    public static List<WebElement> findLayoutDefaultRow(WebDriver driver, String sectionCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-code='"+ sectionCode +"']")));
            return driver.findElements(By.cssSelector("div[data-section-code='"+ sectionCode +"'] .tbody .bp-detail-row"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths not found: " + sectionCode);
            return null;
        }
    }

    public static  List<WebElement> findLayoutFieldPath(WebDriver driver, String sectionCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-code='"+ sectionCode +"']")));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[data-section-code='"+ sectionCode +"'] ")));

            List<WebElement> elements = MainProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        }
        catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Elements with sectionCode '"+ sectionCode +"' in data-path not found");
            return Collections.emptyList();
        }
    }


    public static  List<WebElement> findLayoutDtlFieldPath(WebDriver driver, String sectionCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-code='"+ sectionCode +"']")));

            Thread.sleep(1000);
            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[data-section-code='"+ sectionCode +"']")));

            List<WebElement> elements = MainProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        }
        catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Elements with sectionCode '"+ sectionCode +"' in data-path not found");
            return Collections.emptyList();
        }
    }
}

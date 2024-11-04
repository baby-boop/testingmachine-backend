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
import static testingmachine_backend.process.utils.ProcessPath.*;


public class LayoutProcessSection {
    private static final Logger LOGGER = Logger.getLogger(LayoutProcessSection.class.getName());
    private static final int SHORT_WAIT_SECONDS = 2;

    public static void LayoutFieldFunction(WebDriver driver, String id) {
        List<WebElement> layoutSectionPaths = findLayoutSectionPath(driver, id);
        if(layoutSectionPaths != null){
            for (WebElement sectionPath : layoutSectionPaths) {
                String dataSectionCode = sectionPath.getAttribute("data-section-code");
                waitUtils(driver);
                List<WebElement> layoutPath = findLayoutFieldPath(driver, dataSectionCode);
                processTabElements(driver, layoutPath, id);

                List<WebElement> allActionTabPath = findLayoutActionButton(driver, dataSectionCode);
                if (allActionTabPath != null) {
                    waitUtils(driver);
                    for (WebElement action : allActionTabPath) {
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
                            List<WebElement> layoutDtlPath = findLayoutDtlFieldPath(driver, dataSectionCode);
                            processTabElements(driver, layoutDtlPath, id);
                            break;
                        }
                    }
                }
                List<WebElement> layoutPath1 = findLayoutFieldPath(driver, dataSectionCode);
                processTabElementsFinal(driver, layoutPath1, id);
            }
        }
    }

    public static List<WebElement> findLayoutSectionPath(WebDriver driver, String processId ) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            Thread.sleep(2000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='bp-window-" + processId + "']")));
            return driver.findElements(By.cssSelector("div[id='bp-window-" + processId + "'] div[data-section-code]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "layout 'data-section-code' not found: " + processId);
            return null;
        }
    }

    public static List<WebElement> findLayoutActionButton(WebDriver driver, String sectionCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-code='"+ sectionCode +"']")));
            return driver.findElements(By.cssSelector("div[data-section-code='"+ sectionCode +"'] button[data-action-path]"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "action paths error: " + sectionCode);
            return null;
        }
    }

    public static  List<WebElement> findLayoutFieldPath(WebDriver driver, String sectionCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section-code='"+ sectionCode +"']")));

            WebElement MainProcess = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[data-section-code='"+ sectionCode +"']")));

            List<WebElement> elements = MainProcess.findElements(
                    By.cssSelector("[data-path]")
            );

            Map<String, WebElement> uniqueDataPathElements = getUniqueTabElements(elements);

            return new ArrayList<>(uniqueDataPathElements.values());
        }
        catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Elements with sectionCode '"+ sectionCode +"' and data-path not found");
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

            LOGGER.log(Level.SEVERE, "Elements with sectionCode '"+ sectionCode +"' and data-path not found");
            return Collections.emptyList();
        }
    }
}

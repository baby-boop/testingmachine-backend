package testingmachine_backend.indicator;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.Messages.PopupMessage;
import testingmachine_backend.process.utils.ElementsFunctionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IsIndicatorMessage {

    @Getter
    private static int warningCount = 0;
    @Getter
    private static int errorCount = 0;
    @Getter
    private static int infoCount = 0;
    @Getter
    private static int successCount = 0;

    @IndicatorTabField
    public static final ThreadLocal<List<IndicatorCustomTab>> indicatorCustomTab = ThreadLocal.withInitial(ArrayList::new);

    private static final int SHORT_WAIT_SECONDS = 10;

    public static boolean isErrorMessagePresent(WebDriver driver, String parentId, String indicatorId, String customsideBarName, String sideBarName, String indicatorType, String jsonId) {
        try {
            WebElement messageContainer = waitForElement(driver, By.cssSelector(".brighttheme.ui-pnotify-container"), SHORT_WAIT_SECONDS);
            String messageTitle = messageContainer.findElement(By.cssSelector(".ui-pnotify-title")).getText().toLowerCase();

            if (messageTitle.contains("warning")) {
                return processMessage(driver, "warning", parentId, indicatorId, customsideBarName, sideBarName, indicatorType, jsonId );
            } else if (messageTitle.contains("error")) {
                return processMessage(driver, "error", parentId, indicatorId, customsideBarName, sideBarName, indicatorType, jsonId );
            } else if (messageTitle.contains("success")) {
                return processMessage(driver, "success", parentId, indicatorId, customsideBarName, sideBarName, indicatorType, jsonId );
            }   else if (messageTitle.contains("info")) {
                return processMessage(driver, "info", parentId, indicatorId, customsideBarName, sideBarName, indicatorType, jsonId );
            }
            return false;
        } catch (Exception e) {
            System.out.println("No alert found for process: " + parentId);
            return false;
        }
    }

    private static boolean processMessage(WebDriver driver, String type, String parentId, String indicatorId, String customsideBarName, String sideBarName, String indicatorType, String jsonId) {
        try {
            WebElement messageContent = waitForElement(driver, By.cssSelector(".ui-pnotify-text"), 2);
            String messageText = messageContent.getText();

            switch (type) {
                case "warning":
                    warningCount++;
                    break;
                case "error":
                    errorCount++;
                    break;
                case "success":
                    successCount++;
                    break;
                case "info":
                    infoCount++;
                    break;
            }

            IndicatorCustomTab customTab = new IndicatorCustomTab(parentId, indicatorId, customsideBarName, sideBarName, indicatorType, type, messageText, jsonId,
                    ElementsFunctionUtils.getProcessLogMessages()
                            .stream().filter(detail -> detail.getMetaDataId().equals(indicatorId)).collect(Collectors.toList()),
                    ElementsFunctionUtils.getUniqueEmptyDataPath()
                            .stream().filter(detail -> detail.getMetaDataId().equals(indicatorId)).collect(Collectors.toList()),
                    PopupMessage.getUniquePopupMessages()
                            .stream().filter(detail -> detail.getMetaDataId().equals(indicatorId)).collect(Collectors.toList()),
                    ElementsFunctionUtils.getPopupStandartMessages()
                            .stream().filter(detail -> detail.getMetaDataId().equals(indicatorId)).collect(Collectors.toList()),
                    ElementsFunctionUtils.getRequiredPathMessages()
                            .stream().filter(detail -> detail.getMetaDataId().equals(indicatorId)).collect(Collectors.toList()),
                    ElementsFunctionUtils.getComboMessages()
                            .stream().filter(detail -> detail.getMetaDataId().equals(indicatorId)).collect(Collectors.toList())
                            );
            indicatorCustomTab.get().add(customTab);

            return true;
        } catch (Exception e) {
            System.out.println("Error extracting message for indicator: " + indicatorId);
            return false;
        }
    }

    public static List<IndicatorCustomTab> getIndicatorTabMesssage() {
        return new ArrayList<>(indicatorCustomTab.get());
    }

//    Нэг дата нэмэх бол
    public static void addIndicatorMessage(IndicatorCustomTab customTab) {
        indicatorCustomTab.get().add(customTab);
    }

//    олон дата нэмэх бол
    public static void addAllIndicatorMessages(List<IndicatorCustomTab> customTabs) {
        indicatorCustomTab.get().addAll(customTabs);
    }

    private static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}

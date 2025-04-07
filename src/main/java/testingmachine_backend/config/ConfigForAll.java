package testingmachine_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

@Slf4j
public class ConfigForAll {

    public static void loginForm(WebDriver driver, WebDriverWait wait, String username, String password, String isLoginCheckBox) {

        WebElement userNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));
        userNameField.sendKeys(username);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pass_word")));
        passwordField.sendKeys(password);

        if (isLoginCheckBox != null && !isLoginCheckBox.isEmpty()) {
            WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("isLdap")));
            checkBox.click();
        }

//        WebElement captchaContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'fom-row')]")));
//        if (captchaContainer.isDisplayed()) {
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            // Remove the entire CAPTCHA container div
//            js.executeScript("arguments[0].remove();", captchaContainer);
//            System.out.println("CAPTCHA removed successfully.");
//        }

        passwordField.sendKeys(Keys.ENTER);
    }


    private static final String PORT = ":8181";
    private static final String URL = "/erp-services/RestWS/runJson";
    public static final String GOLOMT_URL = "/javarestapi";
    public static final String API_URL1 = ":8080" + URL;
    public static final String API_URL = PORT + URL;
    public static final String PROCESS_URL = "https://dev.veritech.mn:8181/erp-services/RestWS/runJson";
    public static final String REST_URL = "/restapi";
    public static final String INDICATOR_URL = PORT + REST_URL;
    public static final String CALL_PROCESS = "/mdprocess/renderByTestTool/";
    public static final String CALL_DATAVIEW = "/mdobject/dataview/";
    public static final String CALL_METAVERSE = "/mdform/indicatorList/";
    public static final String CALL_PRODUCT = "/appmenu/mvmodule/";
    public static final String CALL_METHOD = "/mdform/method/";
    public static final String CALL_INDICATOR = "/mdobject/meta/";

//    public static String getLocalIpAddress(){
//        return "172.169.88.205";
//    }

    public static String getLocalIpAddress(){
        return "192.168.192.57";
    }

//    public static String getLocalIpAddress(){
//        return "192.168.1.8";
//    }

//    public static String getLocalIpAddress() {
//        try {
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = interfaces.nextElement();
//                if (networkInterface.getDisplayName().toLowerCase().contains("wi-fi") || networkInterface.getName().startsWith("wlan")) {
//                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
//                    while (addresses.hasMoreElements()) {
//                        InetAddress inetAddress = addresses.nextElement();
//                        if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
//                            return inetAddress.getHostAddress();
//                        }
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return "172.169.88.205";
//    }

}

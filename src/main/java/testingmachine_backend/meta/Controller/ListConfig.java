package testingmachine_backend.meta.Controller;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testingmachine_backend.process.Controller.ProcessController;

import java.time.Duration;

public class ListConfig {

    /*
     * DEV USER CONNECTION


    public static final String BaseUrl = "https://dev.veritech.mn";
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/";

 */

    /*
     * Хишиг арвин UAT USER CONNECTION
     */
    public static final String BaseUrl = "http://"+ ProcessController.getSystemURL()+"";
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/";
    public static final String USERNAME = "admin1";
    public static final String PASSWORD = "Khishigarvin*89";

    /*
    * Summit USER CONNECTION

    public static final String BaseUrl = "http://192.168.60.43";
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "VrSummit@123";
 */

    /*
     * Хишиг арвин prod USER CONNECTION

    public static final String BaseUrl = "http://202.131.244.211";
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/";
    public static final String USERNAME = "admin1";
    public static final String PASSWORD = "Khishigarvin*89";
*/

    public static final int TIMEOUT = 10;

    public static WebDriverWait getWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public static void selectDbFuntion(WebDriver driver,  WebDriverWait wait, String value) {
        try{
            WebElement selectDb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dbName")));
            Select dbSelect = new Select(selectDb);
            dbSelect.selectByValue(value);
        }catch (NoSuchElementException e) {
            System.out.println("Element not found" + e.getMessage());
        }
    }

    public static void selectCompanyFunction(WebDriver driver, WebDriverWait wait, String value) {
        try{
            WebElement clickThat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'login/connectClient')]//h6[contains(text(), '" + value + "')]")));
            clickThat.click();
        }catch (NoSuchElementException e) {
            System.out.println("Element not found" + e.getMessage());
            driver.quit();
        }
    }
}

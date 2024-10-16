package testingmachine_backend.metaverse.Controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MVconfig {

        /*
     * DEV USER CONNECTION
*/

    public static final String BaseUrl = "https://dev.veritech.mn";
    public static final String LoginUrl = BaseUrl + "/login";
    public static final String USERNAME = "shinetsetseg.sh";
    public static final String PASSWORD = "shinee@2023";
    public static final String MainUrl = BaseUrl + "/mdobject/dataview/16413658595761?";
    public static final String tailUrl = "&mmid=164560279791910";
    public static final String MetaverseURL= "/mdform/indicatorRender/";



    public static final int TIMEOUT = 10;

    public static WebDriverWait getWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }
}



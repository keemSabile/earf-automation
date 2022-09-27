package project.EARF;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class webdriver {

    public static WebDriver getDriver(WebDriver driver) throws MalformedURLException {

        WebDriverManager.chromedriver().setup();
        //((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        //prefs.put("download.default_directory", System.getProperty("user.dir")+"\\downloads");
        options.setExperimentalOption("prefs", prefs);

        DesiredCapabilities cap = new DesiredCapabilities();
        //cap.setCapability(ChromeOptions.CAPABILITY, prefs);
        cap.setBrowserName(BrowserType.CHROME);
        //cap.setBrowserName(BrowserType.EDGE);
        //cap.setBrowserName(BrowserType.FIREFOX);
        //cap.setBrowserName(BrowserType.SAFARI);
        cap.setVersion("");
        //driver = new ChromeDriver(options);
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
		//driver = new RemoteWebDriver(new URL("http://selenium__standalone-chrome:4444/wd/hub/"), cap);
        System.out.println("Open Chrome Browser");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("http://172.31.93.36/");
        System.out.println("Open Web Page");

        return driver;

    }

}

package project.EARF;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class webdriver {

    public static WebDriver getDriver(WebDriver driver) throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setBrowserName(BrowserType.CHROME);
        cap.setVersion("");
        //driver = new ChromeDriver();
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
        System.out.println("Open Chrome Browser");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("http://172.31.93.36/");
        System.out.println("Open Web Page");
        return driver;

    }

}

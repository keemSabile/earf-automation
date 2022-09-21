package project.EARF;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class earfApplication extends listener {

    public WebDriver driver;

    @Test
    public void EARFWebPage() throws InterruptedException, IOException {

        String className = this.getClass().getSimpleName();
        String directory = "./screenshots/Screenshot/";
        String ScreenshotNames = (path + className);

        // WebDriver
        driver = webdriver.getDriver(driver);

        // HTML Report
        String logText2 = "<b> " + className + " Test Start</b>";
        Markup m2 = MarkupHelper.createLabel(logText2, ExtentColor.BLUE);
        extentTest.get().log(Status.INFO, m2);

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 240)");
        Thread.sleep(500);

        // Personal Information fill-up
        userInfo.earf(driver);

        // Screenshot
        Screen.capture(driver, className, "<b>User personal information input</b>");

        Thread.sleep(1000);

        try {

            js.executeScript("window.scrollBy(0, 1250)");

            // Application
            Thread.sleep(1000);
            driver.findElement(By.xpath("(//input[@name='iBAS-options'])[7]")).click(); // iBAS Admin
            Thread.sleep(1000);
            driver.findElement(By.xpath("(//input[@name='U2000-options'])[5]")).click();// U2000 Import
            Thread.sleep(1000);

            // Screenshot
            Screen.capture(driver, className,"<b>User input applications</b>");

            Thread.sleep(1000);

            js.executeScript("window.scrollBy(0, 1000)");

            Thread.sleep(1000);

            // Productivity Tools
            driver.findElement(By.xpath("//input[@name='MS-Office-options']")).click();// MS Office
            Thread.sleep(1000);

            // Screenshot
            Screen.capture(driver, className,"<b>User input productivity tools</b>");

            Thread.sleep(1000);

            // Add Tools
            WebElement addTool = driver.findElement(By.xpath("//button[normalize-space()='Add Tools']")); // Add Tools+
            for (int i = 0; i < 2; i++) {
                addTool.click();
            }

            js.executeScript("window.scrollBy(0, 200)");

            Thread.sleep(700);
            driver.findElement(By.name("otherTools.[0].valueName")).sendKeys("Eclipse");
            Thread.sleep(700);
            driver.findElement(By.name("otherTools.[1].valueName")).sendKeys("Java");
            Thread.sleep(700);
            driver.findElement(By.name("otherTools.[2].valueName")).sendKeys("Ubuntu");
            Thread.sleep(1000);

            Screen.capture(driver, className,"<b>User personal information input</b>");

            Thread.sleep(1000);

            // Modify PDF
            driver.findElement(By.xpath("(//button[normalize-space()='Generate PDF'])[1]")).click();
            extentTest.get().log(Status.INFO, "User clicks GeneratePDF");
            Thread.sleep(2000);
            driver.switchTo().alert().accept();
            Thread.sleep(5000);

            extentTest.get().log(Status.INFO, "PDF file downloaded on browser");

            // PDF Assert

            assertPDF.earfpdf(driver);


        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

        } catch (AssertionError e) {
            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

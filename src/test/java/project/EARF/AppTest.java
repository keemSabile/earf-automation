package project.EARF;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.sikuli.script.FindFailed;
import org.testng.AssertJUnit;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class AppTest extends listener {

    ExtentSparkReporter htmlReporter;
    ExtentReports extent;
    ExtentTest test;
    WebDriver driver;
    static final String HOST_URL = "http://localhost:4444/wd/hub";

    @BeforeSuite
    public void extent() {
        htmlReporter = new ExtentSparkReporter(".//reports//EARF-report.html"); // File Directory/Title
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws MalformedURLException {
        System.out.println("Open Chrome Browser");
        WebDriverManager.chromedriver().setup();

        // DesiredCapabilities cap = new DesiredCapabilities();
        // cap.setBrowserName(BrowserType.CHROME);
        // driver = new RemoteWebDriver(new URL(HOST_URL),cap);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/");

    }

    @Test(priority = 7)
    public void EARFWebPage() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        try {

            // HTML Report
            test.assignAuthor("Al-Akeem Abdulla");
            test.assignCategory("EARF PDF Download");
            String logText2 = "<b> " + this.getClass().getName() + " Test Start</b>";
            Markup m2 = MarkupHelper.createLabel(logText2, ExtentColor.BLUE);
            extentTest.get().log(Status.INFO, m2);

            // Before Method Actions
            Thread.sleep(1000);
            extentTest.get().log(Status.INFO, "User goes to EARF web app ");

            userInfo.earf(driver);

            // Application
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 1800)");
            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[@id=\"bss\"]/td[7]/input")).click(); // BSS Admin
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"u2000\"]/td[6]/input")).click();// U2000 Import
            Thread.sleep(1000);

            // Take Screenshot
            File scrnshot3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrnshot3, new File(path + "applications.png"));
            extentTest.get().log(Status.INFO, "[Verify] User input application");

            // Productivity Tools
            js.executeScript("window.scrollTo(0, 2800)");
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"productivity-tools\"]/td[1]/input")).click();// MS Office
            Thread.sleep(1000);

            // Take Screenshot
            File scrnshot4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrnshot4, new File(path + "productivity.png"));
            extentTest.get().log(Status.INFO, "[Verify] User input productivity tool");

            // Add Tools+
            driver.findElement(By.id("addTools")).click();// Add Tools+
            js.executeScript("window.scrollTo(0, 3000)");
            Thread.sleep(1000);
            driver.findElement(By.id("toolsInputElement")).sendKeys("Eclipse");
            driver.findElement(By.id("toolsInputElement1")).sendKeys("Java");
            driver.findElement(By.id("toolsInputElement2")).sendKeys("Ubuntu");
            Thread.sleep(2000);

            // Take Screenshot
            File scrnshot5 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrnshot5, new File(path + "otherTools.png"));
            extentTest.get().log(Status.INFO, "[Verify] User input Other tools+");

            // Modify PDF
            driver.findElement(By.id("copyBtn")).click();
            extentTest.get().log(Status.INFO, "User clicks GeneratePDF");
            Thread.sleep(2000);
            driver.switchTo().alert().accept();
            Thread.sleep(5000);

            extentTest.get().log(Status.INFO, "PDF file downloaded on browser");

            WebElement lastname = driver.findElement(By.id("lastName"));
            WebElement firstname = driver.findElement(By.id("firstName"));
            WebElement middlename = driver.findElement(By.id("middleName"));

            String LNval = lastname.getAttribute("value");
            String FNval = firstname.getAttribute("value");
            String MNval = middlename.getAttribute("value");

            // Verify PDF Output - Assert
            File file = new File(
                    "C://Users//p.alakeem.abdulla//Downloads//" + "EARF_" + LNval + "," + FNval + " " + MNval + ".pdf");
            FileInputStream fis = new FileInputStream(file);
            PDFParser parser = new PDFParser(fis);
            parser.parse();

            COSDocument cosDocument = parser.getDocument();
            PDDocument pdDocument = new PDDocument(cosDocument);
            PDFTextStripper stripper = new PDFTextStripper();
            String data = stripper.getText(pdDocument);

            cosDocument.close();
            pdDocument.close();

            // PDF Assert Output
            AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("employeeNumber")).getAttribute("value")));
            AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("emailInput")).getAttribute("value")));
            AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("departmentInput")).getAttribute("value")));
            AssertJUnit
                    .assertTrue(data.contains(driver.findElement(By.id("contactNumberInput")).getAttribute("value")));
            AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("jobtitleInput")).getAttribute("value")));
            AssertJUnit
                    .assertTrue(data.contains(driver.findElement(By.xpath("//*[@id=\"loc\"]")).getAttribute("value")));
            /*
             * AssertJUnit.assertTrue(data.contains(
             * driver.findElement(By.id("toolsInputElement")).getAttribute("value")));
             * AssertJUnit.assertTrue(data.contains(
             * driver.findElement(By.id("toolsInputElement1")).getAttribute("value")));
             * AssertJUnit.assertTrue(data.contains(
             * driver.findElement(By.id("toolsInputElement2")).getAttribute("value")));
             */
            AssertJUnit.assertTrue(data.contains(LNval));
            AssertJUnit.assertTrue(data.contains(FNval));
            AssertJUnit.assertTrue(data.contains(MNval));

            System.out.println(data);

            /*
             * File file = new File("src/main/resources/images/Capture.PNG"); Screen s = new
             * Screen(); Pattern clickMap = new Pattern(file.getAbsolutePath()); Match match
             * = s.exists(clickMap); Thread.sleep(2000); s.doubleClick(match);
             */

        } catch (NoSuchElementException e) {
            System.out.println("Element Error");

        } catch (AssertionError e) {
            System.out.println("Assertion Error");

        } catch (Exception e) {
            System.out.println("Unknown Error");
        }

    }

    /*
     * TC01 Validation on the fields of Employee's Information(Middle Name) Result:
     * Then i should see Middle Name field highlighted. And i should see message
     * "Please fill out this field"
     */

    @Test(priority = 0)
    public void EARF1stTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC01 Validation on the fields of Employee's Information(Middle Name)"); // Test Case
        // Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Information");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Employee Information
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.id("employeeNumber")).sendKeys("11590");
        driver.findElement(By.id("yes")).click();
        driver.findElement(By.id("converge")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("lastName")).sendKeys("Abdulla");
        driver.findElement(By.id("firstName")).sendKeys("Al-Akeem");
        Thread.sleep(1000);
        driver.findElement(By.id("emailInput")).sendKeys("p.asabdulla@convergeict.com");
        driver.findElement(By.id("departmentInput")).sendKeys("IT");

        Thread.sleep(1000);
        js.executeScript("window.scrollTo(0, 450)");
        Thread.sleep(1000);
        driver.findElement(By.id("contactNumberInput")).sendKeys("09876543210");
        Thread.sleep(1000);
        driver.findElement(By.id("jobtitleInput")).sendKeys("ITAP");
        driver.findElement(By.xpath("//*[@id=\"loc\"]")).click();
        Thread.sleep(1000);

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();// press enter
        Thread.sleep(500);

        // Take Screenshot
        File scrnshot3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot3, new File(path + "employeeMiddleName.png"));
        extentTest.get().log(Status.INFO, "[Verify] User middle name");

    }

    /*
     * TC02 Validation on the fields of Employee's Information(Department) Result:
     * Then i should see Contact Number field highlighted. And i should see message
     * "Please fill out this field".
     */

    @Test(priority = 1)
    public void EARF2ndTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC02 Validation on the fields of Employee's Information(Department)"); // Test Case
        // Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Information");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Employee Information
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.id("employeeNumber")).sendKeys("11590");
        driver.findElement(By.id("yes")).click();
        driver.findElement(By.id("converge")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("lastName")).sendKeys("Abdulla");
        driver.findElement(By.id("firstName")).sendKeys("Al-Akeem");
        driver.findElement(By.id("middleName")).sendKeys("Sabile");
        Thread.sleep(1000);
        driver.findElement(By.id("emailInput")).sendKeys("p.asabdulla@convergeict.com");

        Thread.sleep(1000);
        js.executeScript("window.scrollTo(0, 450)");
        Thread.sleep(1000);
        driver.findElement(By.id("contactNumberInput")).sendKeys("09876543210");
        Thread.sleep(1000);
        driver.findElement(By.id("jobtitleInput")).sendKeys("ITAP");
        driver.findElement(By.xpath("//*[@id=\"loc\"]")).click();
        Thread.sleep(1000);

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();// press enter
        Thread.sleep(500);

        // Take Screenshot
        File scrnshot3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot3, new File(path + "employeeDepartment.png"));
        extentTest.get().log(Status.INFO, "[Verify] User Department");

    }

    /*
     * TC03 Validation on the fields of Employee's Information(Employee Number)
     * Result: Then i should see Employee Number field highlighted. And i should see
     * message "Please fill out this field".
     */

    @Test(priority = 2)
    public void EARF3ndTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC03 Validation on the fields of Employee's Information(Employee Number)"); // Test
        // Case
        // Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Information");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Employee Information
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.id("yes")).click();
        driver.findElement(By.id("converge")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("lastName")).sendKeys("Abdulla");
        driver.findElement(By.id("firstName")).sendKeys("Al-Akeem");
        driver.findElement(By.id("middleName")).sendKeys("Sabile");
        Thread.sleep(1000);
        driver.findElement(By.id("emailInput")).sendKeys("p.asabdulla@convergeict.com");
        driver.findElement(By.id("departmentInput")).sendKeys("IT");
        Thread.sleep(1000);
        js.executeScript("window.scrollTo(0, 450)");
        Thread.sleep(1000);
        driver.findElement(By.id("contactNumberInput")).sendKeys("09876543210");
        Thread.sleep(1000);
        driver.findElement(By.id("jobtitleInput")).sendKeys("ITAP");
        driver.findElement(By.xpath("//*[@id=\"loc\"]")).click();
        Thread.sleep(1000);

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();

        Thread.sleep(500);
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(500);

        // Take Screenshot
        File scrnshot3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot3, new File(path + "employeeNumber.png"));
        extentTest.get().log(Status.INFO, "[Verify] User Employee Number");

    }

    /*
     * TC04 Validation on Contact Number field of Employee's Information(Contact
     * Number) Result: Then i should see only letter 'e' input in the field. And i
     * should see a message "Please enter a number"
     */

    @Test(priority = 3)
    public void EARF4thTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC04 Validation on Contact Number field of Employee's Information(Contact Number)"); // Test
        // Case
        // Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Number Inputs");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Employee Information
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.id("employeeNumber")).sendKeys("11590");
        driver.findElement(By.id("yes")).click();
        driver.findElement(By.id("converge")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("lastName")).sendKeys("Abdulla");
        driver.findElement(By.id("firstName")).sendKeys("Al-Akeem");
        driver.findElement(By.id("middleName")).sendKeys("Sabile");
        Thread.sleep(1000);
        driver.findElement(By.id("emailInput")).sendKeys("p.asabdulla@convergeict.com");
        driver.findElement(By.id("departmentInput")).sendKeys("IT");
        Thread.sleep(1000);

        js.executeScript("window.scrollTo(0, 450)");
        Thread.sleep(1000);
        driver.findElement(By.id("jobtitleInput")).sendKeys("ITAP");
        driver.findElement(By.xpath("//*[@id=\"loc\"]")).click(); // location
        Thread.sleep(1000);
        driver.findElement(By.id("contactNumberInput")).sendKeys("abcdefg");

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();// press enter
        Thread.sleep(500);

        // Take Screenshot
        File scrnshot3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot3, new File(path + "employeeContactNumber.png"));
        extentTest.get().log(Status.INFO, "[Verify] User Contact Number");

    }

    /*
     * TC05 Validation on Email Address field of Employee's Information (Email
     * Address) Result: Then i should see Email Address field highlighted. And i
     * should see message "Please include an '@' in the email address. '*' is
     * missing an '@'.
     */

    @Test(priority = 4)
    public void EARF5thTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC05 Validation on Email Address field of Employee's Information (Email Address)"); // Test
        // Case
        // Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Email Inputs");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Employee Information
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.id("employeeNumber")).sendKeys("11590");
        driver.findElement(By.id("yes")).click();
        driver.findElement(By.id("converge")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("lastName")).sendKeys("Abdulla");
        driver.findElement(By.id("firstName")).sendKeys("Al-Akeem");
        driver.findElement(By.id("middleName")).sendKeys("Sabile");
        Thread.sleep(1000);
        driver.findElement(By.id("emailInput")).sendKeys("*");
        driver.findElement(By.id("departmentInput")).sendKeys("IT");

        Thread.sleep(1000);
        js.executeScript("window.scrollTo(0, 450)");
        Thread.sleep(1000);
        driver.findElement(By.id("contactNumberInput")).sendKeys("09876543210");
        Thread.sleep(1000);
        driver.findElement(By.id("jobtitleInput")).sendKeys("ITAP");
        driver.findElement(By.xpath("//*[@id=\"loc\"]")).click();
        Thread.sleep(1000);

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();// press enter
        Thread.sleep(500);

        // Take Screenshot
        File scrnshot3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot3, new File(path + "employeeEmailAddress.png"));
        extentTest.get().log(Status.INFO, "[Verify] User Employee Email Address");

    }

    /*
     * TC06 Additional textbox for Tools to Specify Result: Then i should see a
     * message "Specify Here" And i should see input text boxes below.
     */

    @Test(priority = 5)
    public void EARF7thTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC06 Additional textbox for Tools to Specify"); // Test Case Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Feature");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Add Tools+
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 2800)");
        Thread.sleep(1000);
        driver.findElement(By.id("addTools")).click();// Add Tools+
        js.executeScript("window.scrollTo(0, 3000)");
        Thread.sleep(1000);
        driver.findElement(By.id("toolsInputElement")).sendKeys("Eclipse");
        driver.findElement(By.id("toolsInputElement1")).sendKeys("Java");
        driver.findElement(By.id("toolsInputElement2")).sendKeys("Ubuntu");
        Thread.sleep(2000);

        // Take Screenshot
        File scrnshot5 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot5, new File(path + "otherTools.png"));
        extentTest.get().log(Status.INFO, "[Verify] User input Other tools+");

    }

    /*
     * TC07 EARF PDF File Download Result: Then i should see the PDF downloading in
     * my browser.
     */

    @Test(priority = 6)
    public void EARF8thTest() throws InterruptedException, IOException, FindFailed {
        System.out.println("Open Web Page");
        String path = ".//screenshot/EARF";

        // HTML Reporter
        test = extent.createTest("TC07 EARF PDF File Download"); // Test Case Title
        test.assignAuthor("Al-Akeem Abdulla");
        test.assignCategory("Validation of Download Output");
        test.log(Status.INFO, "Start of test case,  Open chrome browser"); // Start of Extent Report

        // Before Method Actions
        Thread.sleep(1000);
        extentTest.get().log(Status.INFO, "User goes to EARF web app ");

        // Employee Information
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.id("employeeNumber")).sendKeys("11590");
        driver.findElement(By.id("yes")).click();
        driver.findElement(By.id("converge")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("lastName")).sendKeys("Abdulla");
        driver.findElement(By.id("firstName")).sendKeys("Al-Akeem");
        driver.findElement(By.id("middleName")).sendKeys("Sabile");
        Thread.sleep(1000);
        driver.findElement(By.id("emailInput")).sendKeys("p.asabdulla@convergeict.com");
        driver.findElement(By.id("departmentInput")).sendKeys("IT");
        Thread.sleep(1000);
        js.executeScript("window.scrollTo(0, 450)");
        Thread.sleep(1000);
        driver.findElement(By.id("contactNumberInput")).sendKeys("09876543210");
        Thread.sleep(1000);
        driver.findElement(By.id("jobtitleInput")).sendKeys("ITAP");
        driver.findElement(By.xpath("//*[@id=\"loc\"]")).click();
        Thread.sleep(1000);

        // scroll to the bottom of the web app
        js.executeScript("window.scrollTo(0, 2800)");
        Thread.sleep(1000);

        // Modify PDF
        driver.findElement(By.id("copyBtn")).click();
        extentTest.get().log(Status.INFO, "User clicks GeneratePDF");
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
        Thread.sleep(5000);

        extentTest.get().log(Status.INFO, "PDF file downloaded on browser");

        WebElement lastname = driver.findElement(By.id("lastName"));
        WebElement firstname = driver.findElement(By.id("firstName"));
        WebElement middlename = driver.findElement(By.id("middleName"));

        String LNval = lastname.getAttribute("value");
        String FNval = firstname.getAttribute("value");
        String MNval = middlename.getAttribute("value");

        // Verify PDF Output - Assert
        File file = new File(
                "C://Users//p.alakeem.abdulla//Downloads//" + "EARF_" + LNval + "," + FNval + " " + MNval + ".pdf");
        FileInputStream fis = new FileInputStream(file);
        PDFParser parser = new PDFParser(fis);
        parser.parse();

        COSDocument cosDocument = parser.getDocument();
        PDDocument pdDocument = new PDDocument(cosDocument);
        PDFTextStripper stripper = new PDFTextStripper();
        String data = stripper.getText(pdDocument);

        cosDocument.close();
        pdDocument.close();

        // PDF Assert Output
        AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("employeeNumber")).getAttribute("value")));
        AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("emailInput")).getAttribute("value")));
        AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("departmentInput")).getAttribute("value")));
        AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("contactNumberInput")).getAttribute("value")));
        AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("jobtitleInput")).getAttribute("value")));
        AssertJUnit.assertTrue(data.contains(driver.findElement(By.xpath("//*[@id=\"loc\"]")).getAttribute("value")));
        AssertJUnit.assertTrue(data.contains(LNval));
        AssertJUnit.assertTrue(data.contains(FNval));
        AssertJUnit.assertTrue(data.contains(MNval));

        System.out.println(data);

        extentTest.get().log(Status.INFO, "[Verify] PDF outputs");

    }

    @AfterMethod
    public void teardown() {
        System.out.println("Close Browser");
        driver.close();
        test.info("Test completed, Browser closed"); // End of Extent Report
    }

    @AfterSuite
    public void finish() {
        extent.flush(); // erase any previous data on the report and create a new report
    }
}

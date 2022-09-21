package project.EARF;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class listener implements ITestListener {

	public DesiredCapabilities cap = new DesiredCapabilities();

	// Screenshot Set Up
	static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH");
	static Date d = new Date();
	static String setDate = sdf.format(d);
	public static String dateName = setDate.toString().replace(" ", "_");
	public static String path = "./reports/screenshots/Screenshot/";

	private static ExtentReports extent = ExtentReporter.ReportGenerator() ;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	ExtentTest test;


	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		test = extent.createTest(result.getMethod().getMethodName()); // Test
																	// Case
																	// Title

		extentTest.set(test);
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extent.flush();
		
	}

	@Override
	public void onTestFailure(ITestResult result) {

		String methodName = result.getMethod().getMethodName();
		String exceptionMessage = result.getThrowable().getMessage();
		extentTest.get().fail("<details><summary><b><font color=red>" + "Exception Occured, click to see details:"
				+ "</font></b></summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details> \n");

		WebDriver driver = null;

		String path2 = "./screenshots/FailedScreenshot/";
		String path = "./reports/screenshots/FailedScreenshot/";
		String ScreenshotNames = (path + methodName);
		Object testObject = result.getInstance();
		Class clazz = result.getTestClass().getRealClass();
		try {
			driver = (WebDriver) clazz.getField("driver").get(testObject);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			File scrnshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrnshot, new File(ScreenshotNames + "_" + dateName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		extentTest.get().fail("<b><font color=red>" + "Screenshot of failure" + "</font></b>",
				MediaEntityBuilder.createScreenCaptureFromPath(path2 + methodName + "_" + dateName + ".png").build());

		String logText = "<b>" + methodName + " Failed</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
		extentTest.get().log(Status.FAIL, m);
		
		// TODO Auto-generated method stub
		// test.fail(result.getThrowable());
		

		//try {
		//	Screen.Capture(methodName);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}

	}

//	@Override
//	public void onTestSuccess(ITestResult result) {
//		// TODO Auto-generated method stub
//		String methodName = result.getName();
//		String directory = "./screenshots/Screenshot/";
//		String logText = "<b> " + methodName + " Successful</b>";
//		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
//
//		// Post Screenshot on ExtentReport
//		extentTest.get().log(Status.PASS, m).pass(MediaEntityBuilder
//				.createScreenCaptureFromPath(directory + methodName + "_" + dateName + ".png").build());
//
//	}

	/*
	 * public String takeScreenshot(WebDriver driver, String methodName) throws
	 * IOException { String filename = methodName; String directory =
	 * "./reports/screenshots/FailedScreenshot/"; String path = directory +
	 * filename; // File Directory/Title
	 * 
	 * File screenshot = ((TakesScreenshot)
	 * driver).getScreenshotAs(OutputType.FILE); FileUtils.copyFile(screenshot, new
	 * File(path)); System.out.println("*******************************");
	 * System.out.println("Screenshot stored at: " + path);
	 * System.out.println("*******************************");
	 * 
	 * return path; }
	 */

	public static String getScreenshotName(String methodName) {

		Date d = new Date();
		String filename = methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
		return filename;
	}

}

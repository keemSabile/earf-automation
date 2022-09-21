package project.EARF;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporter {

	static ExtentSparkReporter reporter;
	static ExtentReports extent;
	public ExtentTest test;
	
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

	public static ExtentReports ReportGenerator() {
		String filename = getReportName();
		String directory = System.getProperty("user.dir") + "/reports/";
		new File(directory).mkdirs();
		String path = directory + filename; // File Directory/Title
		reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("Web Automation Results");
		reporter.config().setDocumentTitle("Test Results");
		reporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(reporter);

		// System/Environment
		extent.setSystemInfo("Tester", "Quality Engineer: Keem - (DevOps 3)");
		extent.setSystemInfo("Java", "Version 17");
		extent.setSystemInfo("OS", "Windows 10");
		extent.setSystemInfo("RAM", "16GB");
		extent.setSystemInfo("Browser", "Chrome");
		extent.setSystemInfo("IDE", "Eclipse-2022-03");
		return extent;

	}

	public static String getReportName() {

		Date d = new Date();
		String filename = "Automation Report_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
		return filename;

	}
}

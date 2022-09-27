package project.EARF;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class assertPDF extends listener {

	@SuppressWarnings("unchecked")
	public static void earfpdf(WebDriver driver) throws IOException {


		WebElement lastname = driver.findElement(By.name("lastName"));
		WebElement firstname = driver.findElement(By.name("firstName"));
		WebElement middlename = driver.findElement(By.name("middleName"));

		String LNval = lastname.getAttribute("value");
		String FNval = firstname.getAttribute("value");
		String MNval = middlename.getAttribute("value");

		// Verify PDF Output - Assert
		String pdfloc = "./target/downloads/" + "EARF_" + LNval + "," + FNval + " " + MNval + ".pdf";

		PDDocument pdDocument = PDDocument.load(new File(pdfloc));
		PDFTextStripper stripper = new PDFTextStripper();
		String data = stripper.getText(pdDocument);

		// PDF Assert Output
		AssertJUnit.assertTrue(data.contains(LNval));
		AssertJUnit.assertTrue(data.contains(FNval));
		AssertJUnit.assertTrue(data.contains(MNval));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.name("employeeNo")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.name("email")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.xpath("//input[@id=':ra:']")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.name("contactNo")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.xpath("//input[@id=':rc:']")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.id("immediateHead")).getAttribute("value")));
		extentTest.get().log(Status.PASS,
				"<b>[PDF VERIFY] FullName, EmployeeNumber, Email, Department, ContactNumber, JobTitle, ImmidiateHead</b>");

		System.out.println(data);

		AssertJUnit.assertTrue(data.contains(driver.findElement(By.name("otherTools.[0].valueName")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.name("otherTools.[1].valueName")).getAttribute("value")));
		AssertJUnit.assertTrue(data.contains(driver.findElement(By.name("otherTools.[2].valueName")).getAttribute("value")));
		extentTest.get().log(Status.PASS, "<b>[PDF VERIFY] Other Tools</b>");

		// Convert PDF to Image

		try {
			String sourceDir = pdfloc; // Pdf files are read from this folder
			String destinationDir = "./reports/convertedPDF/"; // converted images from pdf document are saved here
			String directory = "./convertedPDF/";
			File sourceFile = new File(sourceDir);
			File destinationFile = new File(destinationDir);
			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
			}
			if (sourceFile.exists()) {
				System.out.println("Images copied to Folder: " + destinationFile.getName());
				PDDocument document = PDDocument.load(sourceDir);
				List<PDPage> list = document.getDocumentCatalog().getAllPages();
				System.out.println("Total files to be converted -> " + list.size());

				String fileName = sourceFile.getName().replace(".pdf", "");
				int pageNumber = 1;
				for (PDPage page : list) {
					BufferedImage image = page.convertToImage();
					File outputfile = new File(destinationDir + fileName + "_" + pageNumber + ".png");
					System.out.println("Image Created -> " + outputfile.getName());
					ImageIO.write(image, "png", outputfile);
					String logText = "<b> " + "EARF PDF-OUTPUT" + " Successful</b>";
					Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
					extentTest.get().log(Status.PASS, m).pass(MediaEntityBuilder
							.createScreenCaptureFromPath(directory + fileName + "_" + pageNumber + ".png").build());
					pageNumber++;

				}
				document.close();
				System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
			} else {
				System.err.println(sourceFile.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

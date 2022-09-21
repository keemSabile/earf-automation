package project.EARF;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class Screen extends listener {

    static int imageCount = 1;

    public static File capture(WebDriver driver, String screenshotName, String message) throws IOException {

        String directory = "./screenshots/Screenshot/";
        String ScreenshotNames = (path + screenshotName);
        String imageName = dateName + (imageCount++) + ".png";
        Media extentPost = MediaEntityBuilder.createScreenCaptureFromPath(directory + screenshotName + "_" + imageName).build();
        File scrnshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrnshot, new File(ScreenshotNames + "_" +imageName));
        extentTest.get().log(Status.INFO, message, extentPost);

        return scrnshot;


    }

}

package project.EARF;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

public class userInfo extends listener {

    public static void earf(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        // Employee Information
        WebElement rdoAccount = driver.findElement(By.xpath("//input[@value='new']"));
        WebElement rdoCompany = driver.findElement(By.xpath("//input[@value='converge']"));
        WebElement rdoLocation = driver.findElement(By.xpath("//input[@value='Metro Manila']"));
        WebElement txtEmployeeNumber = driver.findElement(By.name("employeeNo"));
        WebElement txtLastName = driver.findElement(By.name("lastName"));
        WebElement txtFirstName = driver.findElement(By.name("firstName"));
        WebElement txtMiddleName = driver.findElement(By.name("middleName"));
        WebElement txtEmail = driver.findElement(By.name("email"));
        WebElement txtContactNumber = driver.findElement(By.name("contactNo"));
        WebElement txtImmidiateHead = driver.findElement(By.id("immediateHead"));
        WebElement txtmanager = driver.findElement(By.name("manager"));
        WebElement ddlDepartment = driver.findElement(By.xpath("//input[@id=':ra:']"));
        WebElement ddlJobTitle = driver.findElement(By.xpath("//input[@id=':rc:']"));

        Actions actions = new Actions(driver);

        rdoLocation.click();
        Thread.sleep(300);
        rdoAccount.click();
        Thread.sleep(300);
        rdoCompany.click();
        Thread.sleep(300);
        txtEmployeeNumber.sendKeys("11590");
        Thread.sleep(300);
        txtLastName.sendKeys("Abdulla");
        Thread.sleep(300);
        txtFirstName.sendKeys("Al-Akeem");
        Thread.sleep(300);
        txtMiddleName.sendKeys("Sabile");
        Thread.sleep(300);
        txtEmail.sendKeys("asabdulla@convergeict.com");
        Thread.sleep(300);
        ddlDepartment.sendKeys("admin");
        actions.sendKeys(Keys.DOWN).build().perform();// press down arrow key
        actions.sendKeys(Keys.ENTER).build().perform();// press enter
        Thread.sleep(300);
        ddlJobTitle.sendKeys("engi");
        actions.sendKeys(Keys.DOWN).build().perform();// press down arrow key
        actions.sendKeys(Keys.ENTER).build().perform();// press enter
        Thread.sleep(300);
        txtContactNumber.sendKeys("09876543210");
        Thread.sleep(300);
        txtImmidiateHead.sendKeys("Mavs");
        Thread.sleep(300);
        txtmanager.sendKeys("Mavs");
        Thread.sleep(1000);


    }


}

 package com.LoginPageTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.loginpage.LoginPage;

public class LoginPageTest {
   WebDriver driver;
    static String fpath = "C:\\Users\\user\\Desktop\\LogainDetailsCRM.xlsx";
    static FileInputStream fis;
    static XSSFWorkbook wb;
    static XSSFSheet sheet;

    @Test
    public void logintest() throws IOException {
        fis = new FileInputStream(fpath);
        wb = new XSSFWorkbook(fis);
        sheet = wb.getSheet("LoginDetails");

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("Reports/ExtentReport.html"); // Report will be saved here
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        ExtentTest test = extent.createTest("Login Test", "Test the login functionality with valid credentials");

        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Launch Browser
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        test.info("Navigating to Login page");

        // Create loginPage object
        LoginPage lp = new LoginPage(driver);

        // Positive Test Case
        test.info("Starting positive test case");
        positiveidpass(lp, test);

        // Negative Test Case 1 (Invalid Username)
        test.info("Starting negative test case 1 (Invalid Username)");
        negativeid(lp, test);

        // Negative Test Case 2 (Invalid Password)
        test.info("Starting negative test case 2 (Invalid Password)");
        negativepass(lp, test);

        wb.close();
        fis.close();
        extent.flush();
        driver.quit(); // Close the browser
    }

    public void positiveidpass(LoginPage lp, ExtentTest test) throws IOException  {
        // Enter credential from excel
        String id = sheet.getRow(1).getCell(0).getStringCellValue();
        String pass = sheet.getRow(1).getCell(1).getStringCellValue();
        lp.EnterPositiveUsername(id, pass);
        String screenshotPath = captureScreenshot();
        test.pass("Successfully logged in with valid credentials",com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    public void negativeid(LoginPage lp, ExtentTest test) throws IOException {
        // Enter credential from excel
        String id = sheet.getRow(2).getCell(0).getStringCellValue();
        String pass = sheet.getRow(2).getCell(1).getStringCellValue();
        lp.EnterNegativeusername(id, pass); // Use EnterNegativeusername for incorrect username
        String screenshotPath = captureScreenshot();
         test.fail("Login failed with invalid username",com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    public void negativepass(LoginPage lp, ExtentTest test) throws IOException {
        // Enter credential from excel
        String id = sheet.getRow(3).getCell(0).getStringCellValue();
        String pass = sheet.getRow(3).getCell(1).getStringCellValue();
        lp.EnterNegativepassowrd(id, pass); // Use EnterNegativepassowrd for incorrect password
        String screenshotPath = captureScreenshot();
        test.fail("Login failed with invalid password", com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }
    
    public String captureScreenshot() throws IOException {
       
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(0));

       
        String screenshotPath = "C:\\Users\\user\\eclipse-workspace\\CapstoneProject_OrangeHRM\\Screenshots" + timestamp + ".png";

       
        File destination = new File(screenshotPath);
        FileHandler.copy(screenshot, destination);

        return screenshotPath;
    }
    
}

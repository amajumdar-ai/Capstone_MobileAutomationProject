package base;

import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import utils.ExtentManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.testng.annotations.Optional;

public class BaseTest {
    protected AndroidDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeSuite
    @Parameters({"deviceName", "platformVersion", "udid", "appiumServerURL"})
    public void setup(
        @Optional("emulator-5556") String deviceName, 
        @Optional("14") String platformVersion, 
        @Optional("emulator-5556") String udid, 
        @Optional("http://127.0.0.1:4723") String appiumServerURL) {
        
        extent = ExtentManager.getInstance();
        test = extent.createTest("Test Setup for " + deviceName);

        try {
            String apkPath = "/Users/arpitamajumdar/Downloads/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setDeviceName(deviceName)
                    .setAutomationName("UiAutomator2")
                    .setPlatformVersion(platformVersion)
                    .setUdid(udid)
                    .setApp(apkPath)
                    .setAppPackage("com.swaglabsmobileapp")
                    .setAppActivity("com.swaglabsmobileapp.MainActivity")
                    .setNewCommandTimeout(Duration.ofSeconds(300));

            System.out.println("Initializing driver with URL: " + appiumServerURL);
            driver = new AndroidDriver(new URL(appiumServerURL), options);
            test.log(Status.PASS, "✅ App launched successfully on " + deviceName);
            driver.runAppInBackground(Duration.ofSeconds(1));  // Simulate background and foreground switch
            driver.activateApp("com.swaglabsmobileapp"); 

        } catch (MalformedURLException e) {
            test.log(Status.FAIL, "❌ Invalid Appium server URL: " + e.getMessage());
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ Failed to initialize AndroidDriver: " + e.getMessage());
        }
    }
    public String captureScreenshot(String testName) {
        try {
            // Get the path dynamically from the project root directory
            String projectDir = System.getProperty("user.dir");
            String screenshotDir = projectDir + "/target/screenshots/";
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = screenshotDir + testName + ".png";
    
            // Ensure the directory exists before saving the screenshot
            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
    
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            System.out.println("✅ Screenshot saved: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("❌ Failed to save screenshot: " + e.getMessage());
            return null; // Return null instead of an empty string
        }
    }
    
    

    public AndroidDriver getDriver() {
        return driver;
    }
}

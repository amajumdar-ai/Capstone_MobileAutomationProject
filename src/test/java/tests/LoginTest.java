package tests;

import base.BaseTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.LoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;
    private WebDriverWait wait;

    @Parameters({"deviceName", "platformVersion", "udid", "appiumServerURL"})
    @BeforeTest
    public void setupTest(
        @Optional("emulator-5556") String deviceName, 
        @Optional("14") String platformVersion, 
        @Optional("emulator-5556") String udid, 
        @Optional("http://127.0.0.1:4723") String appiumServerURL) {

        setup(deviceName, platformVersion, udid, appiumServerURL);
        if (driver == null) {
            throw new IllegalStateException("Driver initialization failed.");
        }
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    @Test
    public void testValidLogin() {
        test = extent.createTest("Valid Login Test on " + driver.getCapabilities().getCapability("deviceName"));

        // Perform login
        loginPage.login("standard_user", "secret_sauce");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        try {
            WebElement homeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Menu\"]") 
            ));

            Assert.assertTrue(homeElement.isDisplayed(), "❌ Homepage did not load correctly!");

            // 📸 Capture Screenshot for Passed Test
            String screenshotPath = captureScreenshot("testValidLogin");
            test.log(Status.PASS, "✅ Login successful, Login screen disappeared.",
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (Exception e) {
            // 📸 Capture Screenshot for Failed Test
            String screenshotPath = captureScreenshot("testValidLogin_Fail");
            test.log(Status.FAIL, "❌ Login test failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            Assert.fail(e.getMessage());
        }
    }
}

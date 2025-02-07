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
import io.netty.handler.timeout.TimeoutException;

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

    // Use explicit wait instead of setting implicit wait to 0
    try {
        WebElement homeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Menu\"]")
        ));

        Assert.assertTrue(homeElement.isDisplayed(), "❌ Homepage did not load correctly!");

        // 📸 Capture Screenshot for Passed Test
        String screenshotPath = captureScreenshot("testValidLogin");
        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            test.log(Status.PASS, "✅ Login successful, Login screen disappeared.",
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            test.log(Status.PASS, "✅ Login successful, Login screen disappeared.");
        }
    } catch (TimeoutException e) {
        test.log(Status.FAIL, "❌ Timeout while waiting for homepage element: " + e.getMessage());
        String screenshotPath = captureScreenshot("testValidLogin_Fail");
        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            test.log(Status.FAIL, "❌ Login test failed due to timeout: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            test.log(Status.FAIL, "❌ Login test failed due to timeout: " + e.getMessage());
        }
        Assert.fail(e.getMessage());
    } catch (Exception e) {
        String screenshotPath = captureScreenshot("testValidLogin_Fail");
        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            test.log(Status.FAIL, "❌ Login test failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            test.log(Status.FAIL, "❌ Login test failed: " + e.getMessage());
        }
        Assert.fail(e.getMessage());
    }
}

}

package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import java.time.Duration;

public class LoginPage {
    private AndroidDriver driver;  // 🔹 Add AndroidDriver instance
    private WebDriverWait wait;

    // Using Accessibility ID instead of ID
    @AndroidFindBy(accessibility = "test-Username")
    private WebElement username;

    @AndroidFindBy(accessibility = "test-Password")
    private WebElement password;

    @AndroidFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    // Constructor
    public LoginPage(AndroidDriver driver) {
        this.driver = driver;  // 🔹 Store the driver instance
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    // Login Method
    public void login(String user, String pass) {
        enterText(username, user, "Username");
        enterText(password, pass, "Password");
        clickElement(loginButton, "Login Button");
    }

    // Utility Methods
    private void enterText(WebElement element, String text, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            System.out.println("✅ Entered text into " + elementName + ": " + text);
        } catch (Exception e) {
            System.err.println("❌ Error entering text into " + elementName + ": " + e.getMessage());
            
        }
    }

    private void clickElement(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            System.out.println("✅ Clicked " + elementName);
        } catch (Exception e) {
            System.err.println("❌ Error clicking " + elementName + ": " + e.getMessage());
           
        }
    }

   public void scrollToElement(String xpath) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().xpath(\"" + xpath + "\"))"
            ));
            System.out.println("✅ Scrolled to element: " + xpath);
        } catch (Exception e) {
            System.err.println("❌ Error scrolling to element: " + xpath + " | " + e.getMessage());
        }
    }
    
    
}

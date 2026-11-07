package BaiTapVeNhaDay3;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;


public class BookYourDemo {
    static WebDriver driver = null;
    private JavascriptExecutor js;

    @BeforeMethod
    public void SetUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://saucelabs.com/request-demo");
    }
    @Test
    public void testCase01_HappyCase_Vietnam() throws InterruptedException {
        WebElement businessMail = driver.findElement(By.id("Business-Email"));
        businessMail.sendKeys("qa.test@example.com");
        WebElement firstName = driver.findElement(By.name("First Name"));
        firstName.sendKeys("Chau");
        WebElement lastName = driver.findElement(By.name("Last Name"));
        lastName.sendKeys("Nguyen");
        WebElement phoneNumber = driver.findElement(By.id("Phone"));
        phoneNumber.sendKeys("0983178335");
        WebElement company = driver.findElement(By.name("Company"));
        company.sendKeys("FPT");
        WebElement countryDropdown = driver.findElement(By.xpath("//select[@id ='Country']"));
        Select selectCountry = new Select(countryDropdown);
        selectCountry.selectByValue("Vietnam");
        WebElement formElement = driver.findElement(By.xpath("//form"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isFormValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", formElement);
        org.testng.Assert.assertTrue(isFormValid, "Kịch bản 1: Form phải hợp lệ!");
        Thread.sleep(Duration.ofMillis(5000));
        driver.quit();
    }
    @Test
    public void testCase02_Chekbutton() throws InterruptedException {
        WebElement businessMail = driver.findElement(By.id("Business-Email"));
        businessMail.sendKeys("hhhh");
        WebElement demoButton = driver.findElement(By.xpath("//button[contains(text(),'request')]"));
        boolean isButtonEnable = demoButton.isEnabled();
        System.out.println("Trạng thái của button submit: " + isButtonEnable);
        demoButton.click();
        Thread.sleep(Duration.ofMillis(1000));
        JavascriptExecutor js0 = (JavascriptExecutor) driver;
        String validationMessage3 = (String) js0.executeScript("return arguments[0].validationMessage;", businessMail);
        System.out.println("Thông báo lỗi: " + validationMessage3 );
        Thread.sleep(Duration.ofMillis(3000));
        driver.quit();
    }
//    public void testCase02_Country_US_Requires_State() throws InterruptedException {
//        WebElement businessMail = driver.findElement(By.id("Business-Email"));
//        businessMail.sendKeys("qa.test@example.com");
//        WebElement firstName = driver.findElement(By.name("First Name"));
//        firstName.sendKeys("Chau");
//        WebElement lastName = driver.findElement(By.name("Last Name"));
//        lastName.sendKeys("Nguyen");
//        WebElement phoneNumber = driver.findElement(By.id("Phone"));
//        phoneNumber.sendKeys("0983178335");
//        WebElement company = driver.findElement(By.name("Company"));
//        company.sendKeys("FPT");
//        WebElement countryDropdown = driver.findElement(By.xpath("//select[@id ='Country']"));
//        Select selectCountry = new Select(countryDropdown);
//        selectCountry.selectByValue("United States");
//        WebElement state = driver.findElement(By.xpath("//select[@id='State']"));
//        Select slState = new Select(state);
//        slState.deselectByIndex(1);
//        WebElement formElement1 = driver.findElement(By.xpath("//form"));
//        JavascriptExecutor js1 = (JavascriptExecutor) driver;
//        Boolean isFormValid1 = (Boolean) js1.executeScript("return arguments[0].checkValidity();", formElement1);
//        org.testng.Assert.assertTrue(isFormValid1, "Kịch bản 1: Form phải hợp lệ!");
//        Thread.sleep(Duration.ofMillis(3000));
//        driver.quit();
//    }
    @Test
    public void testCase03_Missing_Country_Error() throws InterruptedException {
        WebElement businessMail = driver.findElement(By.id("Business-Email"));
        businessMail.sendKeys("qa.test@example.com");
        WebElement firstName = driver.findElement(By.name("First Name"));
        firstName.sendKeys("Chau");
        WebElement lastName = driver.findElement(By.name("Last Name"));
        lastName.sendKeys("Nguyen");
        WebElement phoneNumber = driver.findElement(By.id("Phone"));
        phoneNumber.sendKeys("0983178335");
        WebElement company = driver.findElement(By.name("Company"));
        company.sendKeys("FPT");
        WebElement demoButton0 = driver.findElement(By.xpath("//button[contains(text(),'request')]"));
        demoButton0.click();
        WebElement countryDropdown = driver.findElement(By.xpath("//select[@id ='Country']"));
        JavascriptExecutor js2 = (JavascriptExecutor) driver;
        String validationMessage2 = (String) js2.executeScript("return arguments[0].validationMessage;", countryDropdown);
        System.out.println("Thông báo lỗi: " + validationMessage2 );
        Thread.sleep(Duration.ofMillis(3000));
        driver.quit();
    }
    @Test
    public void testCase04_Blank_Email_Error() throws InterruptedException {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://saucelabs.com/request-demo");
        WebElement demoButton1 = driver.findElement(By.xpath("//button[contains(text(),'request')]"));
        demoButton1.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement businessMail2 = driver.findElement(By.id("Business-Email"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String errorMessage1 = (String) js.executeScript("return arguments[0].validationMessage;", businessMail2);
        System.out.println("Thông báo lỗi: " + errorMessage1 );
        Thread.sleep(3000);
        driver.switchTo().window(tabs.get(0));
        driver.quit();

    }
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    // mở tab mới


        // check error: invalid email

        // WebElement businessMail3 = driver.findElement(By.id("Business-Email"));
        // businessMail3.sendKeys("a@gmail.com");
        // WebElement demoButton2 = driver.findElement(By.xpath("//button[contains(text(),'request')]"));
        // demoButton2.click();
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
       // By errorLocator = By.xpath("//div[contains(text(),'Please enter a valid business email')]");
       // or By errorLocator = By.xpath("//input[@name='Business Email']/following-sibling::div[contains(text(),'Please')]");
      //  WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator));
       // String errorMessage2 = errorElement.getText();
      //  System.out.println("Thông báo lỗi 2: " + errorMessage2);


}

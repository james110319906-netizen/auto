package BaiTapVeNhaDay3;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;
import java.time.Duration;
import java.util.ArrayList;

public class BookYourDemo {
    static WebDriver driver = null;

    static void main() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://saucelabs.com/request-demo");
        WebElement businessMail = driver.findElement(By.id("Business-Email"));
        businessMail.sendKeys("test@gmail.com");
        WebElement company = driver.findElement(By.name("Company"));
        company.sendKeys("FPT");
        //WebElement useCase = driver.findElement(By.xpath("//option[contains(text(),'Real Device')]/ancestor::div[@class='form-field']"));
        // WebElement dropdownUsecase = driver.findElement(By.id("Use-Case"));
        //Select selectUsecase = new Select(dropdownUsecase);
        //selectUsecase.selectByVisibleText("Real Device Testing");
        WebElement demoButton = driver.findElement(By.xpath("//button[contains(text(),'request')]"));
        demoButton.click();
        Thread.sleep(Duration.ofMillis(5000));
        // mở tab mới
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
        driver.switchTo().window(tabs.get(0));
        driver.quit();
    }
}

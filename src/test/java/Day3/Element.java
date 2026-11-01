package Day3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static Day3.BrowserComandsURL.driver;

public class Element {
    static WebDriver driver = null;

    static void main() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://saucelabs.com/request-demo");
       // WebElement requestADemo = driver.findElement(By.xpath("//h3[text()='Request a demo']"));
        // System.out.printf("String Request A Demo la: " + requestADemo.getText());
        //WebElement elementClass = driver.findElement(By.name("Company"));
        //elementClass.sendKeys("FPT Sofware");
        //WebElement eleClassName = driver.findElement(By.className("form-button"));
        //eleClassName.click();
        //WebElement elm1 = driver.findElement(By.id("Business-Email"));
       // //elm1.clear();
        //WebElement header = driver.findElement(By.tagName(p));

        //driver.close(); //chỉ đóng cái đang tương tác
    }

}

package Day3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class byLinktext {
    static WebDriver driver = null;

    static void main() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://saucelabs.com/request-demo");
        //WebElement bylinktext = driver.findElement(By.linkText("Product tours"));
        WebElement bylinktext = driver.findElement(By.partialLinkText("Product"));
        bylinktext.click();

    }


}

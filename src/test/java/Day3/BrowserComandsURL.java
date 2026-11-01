package Day3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class BrowserComandsURL {
    static WebDriver driver = null;

    static void main() throws InterruptedException {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://saucelabs.com/");
            Thread.sleep(Duration.ofMillis(5000));
            driver.navigate().to("url: https://saucelabs.com/request-demo");
            Thread.sleep(Duration.ofMillis(5000));
            driver.navigate().back();
            driver.navigate().forward();
            String url = driver.getCurrentUrl();
            System.out.println("String url la: " + url);
        driver.close(); //chỉ đóng cái đang tương tác
    //    driver.quit();// dong het
    }
}

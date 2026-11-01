package Day3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

public class switchTab {
    public static void main() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://saucelabs.com/request-demo");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        // mở tab mới
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://docs.saucelabs.com/web-apps/automated-testing/selenium/quickstart/");
        Thread.sleep(3000);
        driver.switchTo().window(tabs.get(0));
        //driver.quit();
    }
}

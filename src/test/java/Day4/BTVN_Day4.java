package Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import static java.lang.IO.println;


public class BTVN_Day4 {
    private static Object wait;

    public static void main() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//       Step1 : Login vào https://www.saucedemo.com/
        driver.get("https://www.saucedemo.com/");
        WebElement inputUsername = driver.findElement(By.id("user-name"));
        inputUsername.sendKeys("standard_user");
        WebElement inputPassword = driver.findElement(By.id("password"));
        inputPassword.sendKeys("secret_sauce");
        WebElement buttonLogin = driver.findElement(By.id("login-button"));
        buttonLogin.click();

//      Step 2: Chọn tìm kiếm droplist Price (low to high)

        WebElement dropdownSort = driver.findElement(By.xpath("//select[contains(@class,'sort')]"));
        Select dropSortSelect = new Select(dropdownSort);
        dropSortSelect.selectByValue("lohi");

//      Step 3: Add to cart 2 sản phẩm bất kì

        List<WebElement> addButton = driver.findElements(By.xpath("//button[contains(text(),'Add')]"));
        if (addButton.size() < 2) {
            println("Không tìm đủ số lượng trên trang này");
        } else {
            addButton.get(0).click();
            Thread.sleep(500);
            addButton.get(1).click();
            Thread.sleep(500);
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cart_badge = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class,'shopping_cart_link')]"))
        );
        String cart_count = cart_badge.getText();
        System.out.println("ở giỏ hàng hiển thị số " + cart_count);
       // Step 4: Click vào giỏ hàng

        cart_badge.click();
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='title']"))
        );
        List<WebElement> cart_Item = driver.findElements(By.xpath("//div[@class='cart_item']"));
        List<WebElement> cart_Remove = driver.findElements(By.xpath("//button[text()='Remove']"));
        if(cart_Item.size() == 2){
            System.out.println("Thông tin Your Cart hiển thị đúng 2 sản phẩm với tên và giá tiền đúng");
        }
        else{
            System.out.println("Thông tin Your Cart hiển thị không đúng số lượng, thưc tế: " + cart_Item.size());
        }
        if(cart_Remove.size() == 2){
            System.out.println("Màn hình có hiển thị 2 button Remove");
        }
        else{
            System.out.println("Thông tin Your Cart hiển thị sai số lượng button remove, thưc tế: " + cart_Remove.size());
        }
    }
}

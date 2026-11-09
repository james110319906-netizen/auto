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
        // Click checkout và nhập các thông tin Firts name, Last name, Zip code

        WebElement checkout_btn = driver.findElement(By.id("checkout"));
        checkout_btn.click();
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='title']"))
        );
        WebElement first_Name = driver.findElement(By.id("first-name"));
        WebElement last_Name = driver.findElement(By.id("last-name"));
        WebElement zip_Code = driver.findElement(By.id("postal-code"));
        first_Name.sendKeys("Nguyen");
        last_Name.sendKeys("Minh");
        zip_Code.sendKeys("123");
        //Step 6: Click continute

        WebElement continue_btn = driver.findElement(By.xpath("//input[contains(@id,'continue')]"));
        continue_btn.click();
        Thread.sleep(300);
        List<WebElement> cart_Item1 = driver.findElements(By.xpath("//div[@class='cart_item']"));
        System.out.println("\n--- Kiểm tra danh sách sản phẩm hiển thị ---");
        System.out.println("\nSố lượng sản phẩm trong danh sách: " + cart_Item1.size());
        cart_Item1.forEach(item -> {
            String quantity = item.findElement(By.className("cart_quantity")).getText();
            String name = item.findElement(By.className("inventory_item_name")).getText();
            String price = item.findElement(By.className("inventory_item_price")).getText();
            System.out.println("\nSản phẩm: [Số lượng: " + quantity + " | Tên: " + name + " | Giá: " + price + "]");
        });
        WebElement shippingInfo = driver.findElement(By.xpath("//div[contains(text(),'Shipping Information')]"));
        String shippingText = shippingInfo.getText();

        System.out.println("\n--- Kiểm tra thông tin vận chuyển ---");
        if (shippingText.equals("Free Pony Express Delivery!")) {
            System.out.println("Shipping Information hiển thị ĐÚNG: " + shippingText);
        } else {
            System.out.println("Shipping Information hiển thị SAI! Thực tế là: " + shippingText);
        }
        //--- System.out.println 3: Price Total hiển thị đúng tổng tiền 2 sản phẩm (Item total) ---
        WebElement itemTotalElement = driver.findElement(By.className("summary_subtotal_label"));
        String itemTotalText = itemTotalElement.getText();
        System.out.println("\n--- Kiểm tra tổng tiền sản phẩm ---");
        System.out.println("\nThông tin hiển thị: " + itemTotalText);


// --- System.out.println 4: Total hiển thị đúng tổng tiền của Item total + Tax ---
// Lấy thông tin tiền thuế (Tax) để in thêm cho đầy đủ
        WebElement taxElement = driver.findElement(By.className("summary_tax_label"));
        System.out.println("\nThông tin tiền thuế: " + taxElement.getText());

// Lấy thông tin tổng cộng cuối cùng (Total)
        WebElement totalElement = driver.findElement(By.className("summary_total_label"));
        String totalText = totalElement.getText(); // Ví dụ: "Total: $43.18"
        System.out.println("\nThông tin tổng tiền cuối cùng (gồm thuế): " + totalText);


// --- System.out.println 5: Button Finish hiển thị ---
        WebElement finishButton = driver.findElement(By.id("finish"));
        System.out.println("\n--- Kiểm tra nút Finish ---");
        if (finishButton.isDisplayed()) {
            System.out.println("Button Finish hiển thị thành công: ĐÚNG");
        } else {
            System.out.println("Không tìm thấy Button Finish hoặc nút bị ẩn: SAI");
        }
        //Step 7: Click Finish
        finishButton.click();
        WebElement checkout_Title = driver.findElement(By.xpath("//span[@class='title']"));
        WebElement checkout_Header = driver.findElement(By.xpath("//h2[@class='complete-header']"));
        WebElement checkout_complete_text = driver.findElement(By.xpath("//div[@class='complete-text']"));
        WebElement back_btn = driver.findElement(By.xpath("//button[@id='back-to-products']"));
        String title = checkout_Title.getText();
        System.out.println("\nHien thi title: " + title);
        String header = checkout_Header.getText();
        System.out.println("\nHien thi header: " + header);
        String complete_Text = checkout_complete_text.getText();
        System.out.println("\nHien thi text: " + complete_Text);
        System.out.println("\n--- Kiểm tra nút Back Home ---");
        if (back_btn.isDisplayed()) {
            System.out.println("Button Back Home hiển thị thành công: ĐÚNG");
        } else {
            System.out.println("Không tìm thấy Button Back home hoặc nút bị ẩn: SAI");
        }
    }
}

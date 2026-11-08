package Day3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BÀI GIẢI THAM KHẢO – Form "Book Your Demo"
 * Trang: https://saucelabs.com/request-demo
 *
 * ===================== ĐIỀU CẦN BIẾT VỀ FORM (khám phá bằng tay trước khi code) =====================
 * 1. Lúc mới mở trang chỉ thấy 4 ô: Business Email, Company, Use Case, Comments.
 *    Khi nhập 1 email CÔNG TY hợp lệ, trang mới hiện thêm 4 ô bắt buộc:
 *    First name, Last name, Phone number, Country.
 *    => Phải nhập email TRƯỚC, chờ một chút, rồi mới điền các ô còn lại.
 * 2. Email miễn phí (gmail, yahoo, outlook...) bị chặn:
 *    hiện lỗi "Please enter a valid business email." và nút submit bị khoá.
 * 3. Ô State chỉ hiện (và bắt buộc) khi Country = United States.
 * 4. Use Case luôn có sẵn giá trị "AI Test Authoring" (không có lựa chọn trống).
 * 5. Company tối đa 256 ký tự (maxlength=256).
 * 6. Khi bỏ trống ô bắt buộc, lỗi là popup của TRÌNH DUYỆT (HTML5), không phải phần tử trên trang
 *    => getText() không đọc được; phải đọc thuộc tính "validationMessage" của ô đó.
 * 7. Submit thành công -> chuyển sang trang /thank-you-demo.
 *
 * ===================== LƯU Ý =====================
 * - Đây là website THẬT. Happy case mặc định KHÔNG bấm gửi (SUBMIT_REAL_FORM = false)
 *   để tránh gửi yêu cầu rác tới Sauce Labs. Chỉ bật khi giáo viên cho phép.
 * - Bài này chờ bằng Thread.sleep(). Ở bài sau sẽ học Wait để thay thế cho tốt hơn.
 * - Chrome đang chạy tiếng Việt sẽ hiện thông báo lỗi tiếng Việt, nên ta ép Chrome dùng tiếng Anh.
 * - Dùng dữ liệu GIẢ (example.com, số 0900000000), không dùng email/số điện thoại thật.
 */
public class ChuaBTVN_BookYourDemoTest {

    // ----------------- Cấu hình -----------------
    static final String URL = "https://saucelabs.com/request-demo";
    static final boolean SUBMIT_REAL_FORM = false;   // true = happy case bấm gửi thật

    static final String VALID_EMAIL = "qa.test@example.com";
    static final String MSG_EMPTY_FIELD = "Please fill out this field.";
    static final String MSG_EMPTY_SELECT = "Please select an item in the list.";
    static final String MSG_BUSINESS_EMAIL = "Please enter a valid business email.";

    // ----------------- Locator -----------------
    // Lưu ý: id "First Name" có dấu cách -> dùng By.name cho chắc chắn
    static final By EMAIL      = By.id("Business-Email");
    static final By FIRST_NAME = By.name("First Name");
    static final By LAST_NAME  = By.name("Last Name");
    static final By COMPANY    = By.id("Company");
    static final By PHONE      = By.id("Phone");
    static final By COUNTRY    = By.id("Country");
    static final By STATE      = By.id("State");
    static final By USE_CASE   = By.id("Use-Case");
    static final By COMMENTS   = By.id("Comments");
    static final By CONSENT    = By.id("Checkbox");
    static final By SUBMIT     = By.xpath("//form[@id='wf-form--2']//button[@type='submit']");
    static final By BUSINESS_EMAIL_ERROR =
            By.xpath("//div[text()='" + MSG_BUSINESS_EMAIL + "']");

    static WebDriver driver;
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        driver = new ChromeDriver(createOptions());
        driver.manage().window().maximize();

        try {
            // ===== HAPPY CASE =====
            runTest("TC01 - Happy case: điền đủ thông tin hợp lệ (Vietnam)", () -> tc01_happyCase());
            runTest("TC02 - Happy case: Country = United States + chọn State", () -> tc02_happyCaseUS());

            // ===== UNHAPPY CASE: bỏ trống trường bắt buộc =====
            runTest("TC03 - Bỏ trống Company", () -> tc03_emptyCompany());
            runTest("TC04 - Bỏ trống Business Email", () -> tc04_emptyEmail());
            runTest("TC05 - Bỏ trống First name", () -> tc05_emptyFirstName());
            runTest("TC06 - Bỏ trống Last name", () -> tc06_emptyLastName());
            runTest("TC07 - Bỏ trống Phone number", () -> tc07_emptyPhone());
            runTest("TC08 - Không chọn Country", () -> tc08_noCountry());
            runTest("TC09 - Chọn United States nhưng không chọn State", () -> tc09_usWithoutState());

            // ===== UNHAPPY CASE: email không hợp lệ =====
            runTest("TC10 - Email thiếu @ (abc)", () -> tc10_emailWithoutAt());
            runTest("TC11 - Email thiếu tên miền (abc@)", () -> tc11_emailWithoutDomain());
            runTest("TC12 - Email cá nhân (gmail) bị chặn", () -> tc12_freeEmail());

            // ===== CASE KHÁC =====
            runTest("TC13 - Company nhập 300 ký tự -> chỉ nhận 256", () -> tc13_companyMaxLength());
            runTest("TC14 - Use Case có giá trị mặc định, không có lựa chọn trống", () -> tc14_useCaseDefault());
            runTest("TC15 - Chọn Germany -> hiện checkbox đồng ý, mặc định chưa tick", () -> tc15_consentEurope());
        } finally {
            driver.quit();
        }

        System.out.println("=====================================");
        System.out.println("TỔNG KẾT: PASS = " + passed + ", FAIL = " + failed);
    }

    // =====================================================================
    //                              TEST CASES
    // =====================================================================

    static void tc01_happyCase() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "Nguyen", "QA Training", "0900000000", "Vietnam", "");
        new Select(driver.findElement(USE_CASE)).selectByVisibleText("Cross-Browser Testing");
        driver.findElement(COMMENTS).sendKeys("Automation homework - please ignore.");

        // Mọi trường bắt buộc đều không còn lỗi
        verify(validationMessage(EMAIL).isEmpty(), "Email vẫn báo lỗi: " + validationMessage(EMAIL));
        verify(validationMessage(FIRST_NAME).isEmpty(), "First name vẫn báo lỗi");
        verify(validationMessage(LAST_NAME).isEmpty(), "Last name vẫn báo lỗi");
        verify(validationMessage(COMPANY).isEmpty(), "Company vẫn báo lỗi");
        verify(validationMessage(PHONE).isEmpty(), "Phone vẫn báo lỗi");
        verify(validationMessage(COUNTRY).isEmpty(), "Country vẫn báo lỗi");
        verify(driver.findElement(SUBMIT).isEnabled(), "Nút submit đang bị khoá");

        if (SUBMIT_REAL_FORM) {
            driver.findElement(SUBMIT).click();
            Thread.sleep(5000);
            verify(driver.getCurrentUrl().contains("/thank-you-demo"),
                    "Không chuyển sang trang cảm ơn. URL hiện tại: " + driver.getCurrentUrl());
            String title = driver.findElement(By.tagName("h1")).getText();
            verify(title.contains("Thanks for reaching out"), "Tiêu đề sai: " + title);
        } else {
            System.out.println("   (Không bấm gửi vì SUBMIT_REAL_FORM = false)");
        }
    }

    static void tc02_happyCaseUS() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "Nguyen", "QA Training", "0900000000", "United States", "New York");

        verify(driver.findElement(STATE).isDisplayed(), "Ô State không hiện khi chọn United States");
        verify(validationMessage(STATE).isEmpty(), "State vẫn báo lỗi");
        verify(driver.findElement(SUBMIT).isEnabled(), "Nút submit đang bị khoá");
    }

    // Case trong đề bài
    static void tc03_emptyCompany() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "Nguyen", "", "0900000000", "Vietnam", "");  // chỉ bỏ trống Company
        clickSubmit();
        verifyBlockedBy(COMPANY, MSG_EMPTY_FIELD);
    }

    static void tc04_emptyEmail() throws InterruptedException {
        openForm();
        driver.findElement(COMPANY).sendKeys("QA Training");
        clickSubmit();
        verifyBlockedBy(EMAIL, MSG_EMPTY_FIELD);
        verify(!driver.findElement(FIRST_NAME).isDisplayed(), "First name lẽ ra phải đang ẩn");
    }

    static void tc05_emptyFirstName() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "", "Nguyen", "QA Training", "0900000000", "Vietnam", "");
        clickSubmit();
        verifyBlockedBy(FIRST_NAME, MSG_EMPTY_FIELD);
    }

    static void tc06_emptyLastName() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "", "QA Training", "0900000000", "Vietnam", "");
        clickSubmit();
        verifyBlockedBy(LAST_NAME, MSG_EMPTY_FIELD);
    }

    static void tc07_emptyPhone() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "Nguyen", "QA Training", "", "Vietnam", "");
        clickSubmit();
        verifyBlockedBy(PHONE, MSG_EMPTY_FIELD);
    }

    static void tc08_noCountry() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "Nguyen", "QA Training", "0900000000", "", "");
        clickSubmit();
        verifyBlockedBy(COUNTRY, MSG_EMPTY_SELECT);
    }

    static void tc09_usWithoutState() throws InterruptedException {
        openForm();
        fillForm(VALID_EMAIL, "An", "Nguyen", "QA Training", "0900000000", "United States", "");
        clickSubmit();
        verifyBlockedBy(STATE, MSG_EMPTY_SELECT);
    }

    static void tc10_emailWithoutAt() throws InterruptedException {
        openForm();
        enterEmail("abc");
        clickSubmit();
        String message = validationMessage(EMAIL);
        verify(isStillOnForm(), "Form lẽ ra không được gửi");
        verify(message.contains("missing an '@'"), "Thông báo sai: " + message);
        verify(!driver.findElement(FIRST_NAME).isDisplayed(), "First name lẽ ra phải đang ẩn");
    }

    static void tc11_emailWithoutDomain() throws InterruptedException {
        openForm();
        enterEmail("abc@");
        clickSubmit();
        String message = validationMessage(EMAIL);
        verify(isStillOnForm(), "Form lẽ ra không được gửi");
        verify(message.contains("following '@'"), "Thông báo sai: " + message);
    }

    static void tc12_freeEmail() throws InterruptedException {
        openForm();
        enterEmail("qa.test@gmail.com");

        WebElement error = driver.findElement(BUSINESS_EMAIL_ERROR);
        verify(error.isDisplayed(), "Không hiện lỗi: " + MSG_BUSINESS_EMAIL);
        verify(!driver.findElement(SUBMIT).isEnabled(), "Nút submit lẽ ra phải bị khoá");
        verify(!driver.findElement(FIRST_NAME).isDisplayed(), "First name lẽ ra phải đang ẩn");
    }

    static void tc13_companyMaxLength() throws InterruptedException {
        openForm();
        String text300 = "A".repeat(300);
        WebElement company = driver.findElement(COMPANY);
        company.sendKeys(text300);

        int actualLength = company.getAttribute("value").length();
        verify(actualLength == 256, "Độ dài thực tế = " + actualLength + ", mong đợi 256");
    }

    static void tc14_useCaseDefault() throws InterruptedException {
        openForm();
        Select useCase = new Select(driver.findElement(USE_CASE));

        String defaultOption = useCase.getFirstSelectedOption().getText();
        verify(defaultOption.equals("AI Test Authoring"), "Giá trị mặc định là: " + defaultOption);

        List<WebElement> options = useCase.getOptions();
        verify(options.size() == 12, "Số lựa chọn = " + options.size() + ", mong đợi 12");
        for (WebElement option : options) {
            verify(!option.getAttribute("value").isEmpty(), "Có lựa chọn trống trong Use Case");
        }
    }

    static void tc15_consentEurope() throws InterruptedException {
        openForm();
        enterEmail(VALID_EMAIL);
        verify(!driver.findElement(CONSENT).isDisplayed(), "Checkbox lẽ ra đang ẩn");

        new Select(driver.findElement(COUNTRY)).selectByVisibleText("Germany");
        Thread.sleep(1000);
        WebElement consent = driver.findElement(CONSENT);
        verify(consent.isDisplayed(), "Checkbox không hiện khi chọn Germany");
        verify(!consent.isSelected(), "Checkbox lẽ ra chưa được tick");
    }

    // =====================================================================
    //                              HÀM HỖ TRỢ
    // =====================================================================

    /** Ép Chrome dùng tiếng Anh để thông báo lỗi luôn là tiếng Anh. */
    static ChromeOptions createOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "en-US,en");
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

    static void openForm() throws InterruptedException {
        driver.get(URL);
        Thread.sleep(3000);  // chờ trang tải xong
    }

    /** Nhập email rồi click sang ô khác để trang kiểm tra email và hiện các ô còn lại. */
    static void enterEmail(String email) throws InterruptedException {
        WebElement emailBox = driver.findElement(EMAIL);
        emailBox.clear();
        emailBox.sendKeys(email);
        driver.findElement(COMPANY).click();
        Thread.sleep(1500);  // chờ các ô First name, Phone, Country hiện ra
    }

    /**
     * Điền form. Truyền "" cho trường nào muốn BỎ TRỐNG.
     * Thứ tự quan trọng: nhập email trước, các ô khác chỉ hiện sau khi email hợp lệ.
     */
    static void fillForm(String email, String firstName, String lastName, String company,
                         String phone, String country, String state) throws InterruptedException {
        if (!email.isEmpty()) {
            enterEmail(email);
        }
        typeIfNotEmpty(FIRST_NAME, firstName);
        typeIfNotEmpty(LAST_NAME, lastName);
        typeIfNotEmpty(COMPANY, company);
        typeIfNotEmpty(PHONE, phone);

        if (!country.isEmpty()) {
            new Select(driver.findElement(COUNTRY)).selectByVisibleText(country);
            Thread.sleep(1000);  // chờ ô State hiện ra (nếu chọn United States)
        }
        if (!state.isEmpty()) {
            new Select(driver.findElement(STATE)).selectByVisibleText(state);
        }
    }

    static void typeIfNotEmpty(By locator, String text) {
        if (!text.isEmpty()) {
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
        }
    }

    static void clickSubmit() throws InterruptedException {
        driver.findElement(SUBMIT).click();
        Thread.sleep(1500);
    }

    /** Đọc thông báo lỗi HTML5 của một ô. Chuỗi rỗng = ô hợp lệ. */
    static String validationMessage(By locator) {
        return driver.findElement(locator).getAttribute("validationMessage");
    }

    static boolean isStillOnForm() {
        return driver.getCurrentUrl().contains("/request-demo");
    }

    /** Form bị chặn: vẫn ở trang form VÀ đúng ô cần kiểm tra báo đúng thông báo. */
    static void verifyBlockedBy(By field, String expectedMessage) {
        verify(isStillOnForm(), "Form lẽ ra không được gửi. URL: " + driver.getCurrentUrl());
        String actual = validationMessage(field);
        verify(actual.equals(expectedMessage),
                "Thông báo mong đợi: '" + expectedMessage + "' - thực tế: '" + actual + "'");
    }

    /** Nếu điều kiện sai -> ném lỗi kèm lý do, test được tính là FAIL. */
    static void verify(boolean condition, String failMessage) {
        if (!condition) {
            throw new AssertionError(failMessage);
        }
    }

    // ----------------- Chạy từng test, test lỗi không làm dừng test khác -----------------
    interface TestCase {
        void run() throws Exception;
    }

    static void runTest(String name, TestCase testCase) {
        try {
            testCase.run();
            passed++;
            System.out.println("PASS  " + name);
        } catch (AssertionError e) {
            failed++;
            System.out.println("FAIL  " + name + "\n      -> " + e.getMessage());
        } catch (Exception e) {
            failed++;
            System.out.println("ERROR " + name + "\n      -> " + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
    }
}

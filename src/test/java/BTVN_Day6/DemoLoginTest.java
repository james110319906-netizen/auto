package BTVN_Day6;

import action.DemoLoginAction;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

public class DemoLoginTest {
        WebDriver driver;
        DemoLoginAction demoLoginAction;

        @BeforeMethod
        public void setUp() {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("https://saucelabs.com/request-demo");
            demoLoginAction = new DemoLoginAction(driver);
        }

        @Test(priority = 1, description = "TC01 - Happy - Submit valid form")
        public void TC01_HappySubmitValidForm() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "Chau",
                    "Nguyen",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    "Testing demo form"
            );
            Assert.assertTrue(demoLoginAction.isSuccessMessageDisplayed(),
                    "TC01 Failed: Form không submit thành công với dữ liệu hợp lệ.");
        }

        @Test(priority = 2, description = "TC02 - Unhappy - Business Email empty")
        public void TC02_UnhappyBusinessEmailEmpty() {
            demoLoginAction.actionDemoLogin(
                    "",
                    "Chau",
                    "Nguyen",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC02 Failed: Không hiển thị lỗi khi bỏ trống Business Email.");
        }

        @Test(priority = 3, description = "TC03 - Unhappy - Email missing @")
        public void TC03_UnhappyEmailMissingAt() {
            demoLoginAction.actionDemoLogin(
                    "://studentexample.com",
                    "Chau",
                    "Nguyen",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC03 Failed: Không hiển thị lỗi khi Email thiếu ký tự @.");
        }

        @Test(priority = 4, description = "TC04 - Unhappy - Email missing domain")
        public void TC04_UnhappyEmailContainsSpaces() {
            demoLoginAction.actionDemoLogin(
                    "ch @example.com",
                    "Chau",
                    "Nguyen",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC05 Failed: Không hiển thị lỗi khi Email chứa khoảng trắng.");
        }

//        @Test(priority = 5, description = "TC05 - Unhappy - Gmail/Personal email blocked")
//        public void TC05_UnhappyGmailBlocked() {
//            demoLoginAction.actionDemoLogin(
//                    "test@gmail.com",
//                    "Chau",
//                    "Nguyen",
//                    "Example QA Company",
//                    "0123456789",
//                    "Vietnam",
//                    "",
//                    "AI Test Authoring",
//                    ""
//            );
//            Assert.assertTrue(demoLoginAction.isBusinessEmailErrorDisplayed(),
//                    "TC06 Failed: Không hiển thị lỗi chặn email cá nhân (gmail.com).");
//        }

        @Test(priority = 6, description = "TC06 - Unhappy - First Name empty")
        public void TC06_UnhappyFirstNameEmpty() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "",
                    "Nguyen",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC07 Failed: Không hiển thị lỗi khi bỏ trống First Name.");
        }

        @Test(priority = 7, description = "TC07 - Unhappy - Last Name empty")
        public void TC07_UnhappyLastNameEmpty() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "Chau",
                    "",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC08 Failed: Không hiển thị lỗi khi bỏ trống Last Name.");
        }

        @Test(priority = 8, description = "TC08 - Unhappy - Company empty")
        public void TC08_UnhappyCompanyEmpty() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "chau",
                    "nguyen",
                    "",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(), "TC09 Failed: Không hiển thị lỗi khi bỏ trống Company.");
        }

        @Test(priority = 9, description = "TC09 - Unhappy - Phone number empty")
        public void TC09_UnhappyPhoneEmpty() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "chau",
                    "nguyen",
                    "Example QA Company",
                    "",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC10 Failed: Không hiển thị lỗi khi bỏ trống Phone Number.");
        }

        @Test(priority = 10, description = "TC10 - Unhappy - Country empty")
        public void TC10_UnhappyCountryEmpty() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "chau",
                    "nguyen",
                    "Example QA Company",
                    "0123456789",
                    "",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC11 Failed: Không hiển thị lỗi khi bỏ trống Country.");
        }

        @Test(priority = 11, description = "TC11 - Unhappy - State empty for United States")
        public void TC11_UnhappyStateEmptyForUS() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "chau",
                    "nguyen",
                    "Example QA Company",
                    "0123456789",
                    "United States",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
                    "TC12 Failed: Không hiển thị lỗi khi chọn United States nhưng bỏ trống State.");
        }

//        @Test(priority = 12, description = "TC12 - Boundary - Default Use Case")
//        public void TC12_BoundaryDefaultUseCase() {
//            demoLoginAction.actionDemoLogin(
//                    "test@example.com",
//                    "chau",
//                    "nguyen",
//                    "Example QA Company",
//                    "0123456789",
//                    "Vietnam",
//                    "",
//                    "",
//                    ""
//            );
//            Assert.assertTrue(demoLoginAction.isSubmitsionErrorDisplayed(),
//                    "TC13 Failed: Không hiển thị lỗi khi để mặc định (chưa chọn) Use Case.");
//        }

        @Test(priority = 13, description = "TC13 - Boundary - Use Case options")
        public void TC13_BoundaryUseCaseOptions() {
            int optionCount = demoLoginAction.getUseCaseOptionsCount();
            Assert.assertTrue(optionCount > 1,
                    "TC14 Failed: Dropdown Use Case không có đủ danh sách lựa chọn (số lượng hiện tại: " + optionCount + ").");
        }

        @Test(priority = 14, description = "TC14 - Boundary - Comments optional")
        public void TC14_BoundaryCommentsOptional() {
            demoLoginAction.actionDemoLogin(
                    "test@example.com",
                    "chau",
                    "nguyen",
                    "Example QA Company",
                    "0123456789",
                    "Vietnam",
                    "",
                    "AI Test Authoring",
                    ""
            );
            Assert.assertTrue(demoLoginAction.isSuccessMessageDisplayed(),
                    "TC15 Failed: Không submit được khi bỏ trống trường không bắt buộc Comments.");
        }

        @Test(priority = 15, description = "TC15 - Boundary - Comments maxlength")
        public void TC15_BoundaryCommentsMaxlength() {
            String longComment = "A".repeat(1000);
            demoLoginAction.enterComments(longComment);
            String actualValue = demoLoginAction.getCommentsValue();

            Assert.assertTrue(actualValue.length() <= 1000 && actualValue.length() > 0,
                    "TC17 Failed: Trường Comments xử lý độ dài ký tự không đúng (độ dài thực tế: " + actualValue.length() + ").");
        }


        @Test(priority = 16, description = "TC16 - Happy - Select valid State")
//        public void TC16_HappySelectValidState() {
//            demoLoginAction.actionDemoLogin(
//                    "test@example.com",
//                    "chau",
//                    "nguyen",
//                    "Example QA Company",
//                    "0123456789",
//                    "United States",
//                    "California",
//                    "AI Test Authoring",
//                    "Practice state"
//            );
//            Assert.assertTrue(demoLoginAction.isSuccessMessageDisplayed(),
//                    "TC19 Failed: Không submit được khi chọn United States và State hợp lệ (California).");
//        }


        @AfterMethod
        public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }

}

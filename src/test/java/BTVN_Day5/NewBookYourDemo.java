package BTVN_Day5;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import until.ExelUntils;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Reporter.log;

public class NewBookYourDemo {
    private static final String EXCEL_PATH = "data.xlsx";
    private static final String SHEET_NAME = "Sheet1";
    private static final String LOGIN_URL = "https://saucelabs.com/request-demo";


    public static void main(String[] args) {
        List<Map<String, String>> testData = ExelUntils.getExcelData(EXCEL_PATH, SHEET_NAME);
        WebDriver driver;

        for (Map<String, String> row : testData) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Lấy dữ liệu từng cột từ Excel
            String id = row.get("ID");
            String email = row.get("Business Email");
            String firstName = row.get("First Name");
            String lastName = row.get("Last Name");
            String company = row.get("Company");
            String phone = row.get("Phone Number");
            String country = row.get("Country");
            String state = row.get("State");
            String useCase = row.get("Use Case");
            String comments = row.get("Comments");

            System.out.println("==================================================");
            System.out.println("Đang chạy Case ID: " + id + " Note: " + comments);

            try {
                driver.get(LOGIN_URL);
                // Chờ ô Email xuất hiện
                WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Business-Email")));

                // 1. Nhập dữ liệu vào các ô Text Input
                WebElement businessMail = driver.findElement(By.id("Business-Email"));
                WebElement first_name = driver.findElement(By.name("First Name"));
                WebElement last_Name = driver.findElement(By.name("Last Name"));
                WebElement phoneNumber = driver.findElement(By.id("Phone"));
                WebElement company1 = driver.findElement(By.name("Company"));
                businessMail.sendKeys(email);
                first_name.sendKeys(firstName);
                last_Name.sendKeys(lastName);
                phoneNumber.sendKeys(phone);
                company1.sendKeys(company);
                WebElement countryDropdown = driver.findElement(By.xpath("//select[@id ='Country']"));
                Select selectCountry = new Select(countryDropdown);
                selectCountry.selectByValue(country);
                WebElement state1 = driver.findElement(By.xpath("//select[@id='State']"));
                Select slState = new Select(state1);
                slState.selectByValue(String.valueOf(state));
                WebElement submitBtn = driver.findElement(By.xpath("//button[@type ='submit']"));
                js.executeScript("arguments.scrollIntoView({block: 'center'});", submitBtn);
                submitBtn.click();

                if (id.equals("01") || id.equals("02")) {
                    // --- NHÓM HAPPY CASE (01, 02) ---
                    WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[contains(text(),'Thanks')]")));
                    String case1 = successMsg.getText();
                    System.out.println("Happy case: " + case1);

                    List<WebElement> selects = driver.findElements(By.tagName("select"));
                    WebElement useCaseField = selects.get(selects.size() - 1);
                    Select useCaseSelect = new Select(useCaseField);

                    if (id.equals("03")) {
                        String defaultVal = useCaseSelect.getFirstSelectedOption().getText().trim();
                        System.out.println("-> [CHECK DATA CASE 03] Giá trị default của Use Case là: '" + defaultVal + "'");
                    } else if (!useCase.isEmpty()) {
                        String cleanUseCase = useCase.replaceAll("\\r?\\n", " ").trim();
                        for (WebElement opt : useCaseSelect.getOptions()) {
                            if (opt.getText().trim().toLowerCase().contains(cleanUseCase.toLowerCase())) {
                                opt.click();
                                break;
                            }
                        }
                    }
                }
                else {
                    // UNHAPPY CASE (04 -> 12): Chờ 1.5s và in ra lỗi hiển thị
                    Thread.sleep(1500);
                    boolean foundError = false;

                    // Kiểm tra lỗi hiển thị trên giao diện (Marketo / Webflow)
                    List<WebElement> uiErrors = driver.findElements(By.xpath(
                            "//div[contains(@class,'mktoErrorMsg') or contains(@class,'error') or contains(text(),'Oops! Something went wrong') or contains(text(),'required') or contains(text(),'valid email')]"
                    ));
                    for (WebElement err : uiErrors) {
                        if (err.isDisplayed() && !err.getText().trim().isEmpty()) {
                            System.out.println("-> [UNHAPPY CASE - LỖI UI]: " + err.getText().trim());
                            foundError = true;
                        }
                    }

                    // Kiểm tra lỗi validation mặc định của trình duyệt HTML5
                    List<WebElement> inputs = driver.findElements(By.xpath("//form//input | //form//select"));
                    for (WebElement input : inputs) {
                        Boolean isValid = (Boolean) js.executeScript("return arguments.checkValidity();", input);
                        if (Boolean.FALSE.equals(isValid)) {
                            String msg = (String) js.executeScript("return arguments.validationMessage;", input);
                            String name = input.getAttribute("name") != null ? input.getAttribute("name") : input.getAttribute("id");
                            System.out.println("-> [UNHAPPY CASE - LỖI HTML5 ở '" + name + "']: " + msg);
                            foundError = true;
                        }
                    }
                    if (!foundError) {
                        System.out.println("-> [UNHAPPY CASE - CẢNH BÁO]: Không tìm thấy thông báo lỗi nào hiển thị!");
                    }
                }
            } catch (Exception e) {
                System.out.println("-> [EXCEPTION ở Case " + id + "]: " + e.getMessage());
            } finally {
                driver.quit();
            }
        }
    }
}

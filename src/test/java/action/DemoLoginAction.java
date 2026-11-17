package action;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.LoginDemoPageUI;
import ui.LoginPageUI;

import java.time.Duration;
import java.util.List;


public class DemoLoginAction {
    WebDriverWait wait = null;
    WebDriver driver;
    public DemoLoginAction(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 1. Dùng cho các ô LUÔN PHẢI NHẬP (Email, First Name, Last Name, Company, Phone)
    public void enterRequiredText(By locator, String value) {
        if (value != null && !value.isEmpty()) {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(value);
        }
    }

    // 2. Dùng cho các ô CÓ THỂ BỊ ẨN (như State khi chọn Vietnam, hoặc Comments nếu bỏ trống)
    public void enterOptionalText(By locator, String value) {
        if (value != null && !value.isEmpty()) {
            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
                WebElement element = shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                element.clear();
                element.sendKeys(value);
            } catch (Exception ignored) {
            }
        }
    }

    public void selectDropdownByText(By locator, String text) {
        if (text != null && !text.trim().isEmpty()) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                Select select = new Select(elements.get(0));
                select.selectByVisibleText(text.trim());
            }
        }
    }

    public void enterEmail(String email) {
        enterRequiredText(LoginDemoPageUI.EMAIL_FIELD, email);
    }

    public void enterFirstName(String firstName) {
        enterRequiredText(LoginDemoPageUI.FIRSTNAME_FIELD, firstName);
    }

    public void enterLastName(String lastName) {
        enterRequiredText(LoginDemoPageUI.LASTNAME_FIELD, lastName);
    }

    public void enterCompany(String company) {
        enterRequiredText(LoginDemoPageUI.COMPANY_FIELD, company);
    }

    public void enterPhoneNumber(String phonenumber) {
        enterRequiredText(LoginDemoPageUI.PHONE_FIELD, phonenumber);
    }

    public void selectCountry(String country) {
        selectDropdownByText(LoginDemoPageUI.COUNTRY, country);
    }

    public void selectState(String state) {
        selectDropdownByText(LoginDemoPageUI.STATE, state);
    }

    public void selectUseCase(String useCase) {
        selectDropdownByText(LoginDemoPageUI.USE_CASE, useCase);
    }

    public void enterComments(String comments) {
        enterOptionalText(LoginDemoPageUI.COMMENTS, comments);
    }

    public void clickLogin() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(LoginDemoPageUI.REQUEST_BUTTON));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        try {
            btn.click();
        } catch (Exception e) {
            // Chỉ dùng JS click nếu bị banner che mất nút
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void actionDemoLogin(String email, String firstName, String lastName, String company,
                                String phonenumber, String country, String state, String useCase, String comments) {
        enterEmail(email);

        // Kiểm tra thực tế trên UI xem ô First Name có mở ra không (chờ tối đa 2 giây)
        // Nếu email rỗng (TC02), thiếu @ (TC03), có dấu cách (TC04) hoặc bị chặn @gmail.com (TC05), ô này sẽ không mở ra
        boolean isFormExpanded = false;
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement fn = shortWait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.FIRSTNAME_FIELD));
            isFormExpanded = fn.isDisplayed();
        } catch (Exception ignored) {
        }

        // Chỉ điền các trường mở rộng khi form thực sự mở ra (TC01)
        if (isFormExpanded) {
            enterFirstName(firstName);
            enterLastName(lastName);
            enterPhoneNumber(phonenumber);
        }

        // Các trường bên dưới vẫn nhập đầy đủ cho mọi case
        enterCompany(company);
        selectCountry(country);
        selectState(state);
        selectUseCase(useCase);
        enterComments(comments);

        clickLogin();
    }

    public String getSelectedUseCase() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.USE_CASE)));
        return select.getFirstSelectedOption().getText().trim();
    }

    public int getUseCaseOptionsCount() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.USE_CASE)));
        List<WebElement> options = select.getOptions();
        return options.size();
    }
    public String getCommentsValue() {
        return driver.findElement(LoginDemoPageUI.COMMENTS).getAttribute("value");
    }public boolean isStateFieldDisplayed() {
        try {
            return driver.findElement(LoginDemoPageUI.STATE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isSuccessMessageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.THANK_YOU));
            return msg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.THANK_YOU)).getText();
    }

    public boolean isSubmitsionErrorDisplayed() {
        try {
            // 1. Giữ nguyên check lỗi HTML5 mặc định của trình duyệt (TC02, TC03, TC04)
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Boolean hasHtml5Error = (Boolean) js.executeScript(
                    "var inputs = document.querySelectorAll('input, select, textarea');" +
                            "for (var i = 0; i < inputs.length; i++) {" +
                            "    if (inputs[i].validationMessage && inputs[i].validationMessage.length > 0) {" +
                            "        return true;" +
                            "    }" +
                            "}" +
                            "return false;"
            );
            if (Boolean.TRUE.equals(hasHtml5Error)) {
                return true;
            }

            // 2. Check dòng chữ báo lỗi đỏ của HubSpot trên giao diện (TC05: Please enter a valid business email.)
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return shortWait.until(d -> {
                List<WebElement> errors = d.findElements(By.xpath("//label[contains(@class,'hs-error-msg')] | //*[contains(text(),'valid business email')]"));
                for (WebElement err : errors) {
                    if (err.isDisplayed()) {
                        return true;
                    }
                }
                return false;
            });
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubmitsionError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.SUBMIT_ERROR)).getText();
    }

    public boolean isBusinessEmailErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.EMAIL_ERROR)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getBusinessEmailError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LoginDemoPageUI.EMAIL_ERROR)).getText();
    }
}

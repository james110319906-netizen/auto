package ui;

import org.openqa.selenium.By;

public class LoginDemoPageUI {
    private static final String DEFAULT_USE_CASE = "AI Test Authoring";
    public static final By EMAIL_FIELD = By.id("Business-Email");
    public static final By FIRSTNAME_FIELD = By.name("First Name");
    public static final By LASTNAME_FIELD = By.name("Last Name");
    public static final By COMPANY_FIELD = By.name("Company");
    public static final By PHONE_FIELD = By.id("Phone");
    public static final By COUNTRY = By.id("Country");
    public static final By STATE = By.id("State");
    public static final By USE_CASE = By.id("Use-Case");
    public static final By COMMENTS = By.id("Comments");
    public static final By REQUEST_BUTTON = By.xpath(
            "//button[contains(translate(normalize-space(.),"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                    + "'abcdefghijklmnopqrstuvwxyz'),"
                    + "'request a demo')]"
    );
    public static final By EMAIL_ERROR = By.xpath("//*[contains(text(),'Please enter a valid business email')]");
    public static final By SUBMIT_ERROR = By.cssSelector(".w-form-fail");
    public static final By THANK_YOU = By.xpath("//*[normalize-space()='Thanks for reaching out!']");
}

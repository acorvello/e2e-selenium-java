package io.github.acorvello.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Juice Shop login page. */
public class LoginPage extends BasePage {

    private static final By EMAIL = By.id("email");
    private static final By PASSWORD = By.id("password");
    private static final By SUBMIT = By.id("loginButton");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage waitUntilDisplayed() {
        waitVisible(EMAIL);
        waitVisible(PASSWORD);
        return this;
    }

    public LoginPage loginWith(String email, String password) {
        type(EMAIL, email);
        type(PASSWORD, password);
        safeClick(SUBMIT);
        return this;
    }

    public String errorMessage() {
        return waitVisible(ERROR_MESSAGE).getText();
    }
}

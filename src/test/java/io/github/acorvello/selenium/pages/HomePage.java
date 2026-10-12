package io.github.acorvello.selenium.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Juice Shop home page. */
public class HomePage extends BasePage {

    private static final By SEARCH_BAR = By.cssSelector("app-mat-search-bar");
    private static final By PRODUCTS = By.cssSelector(".product");
    private static final By ACCOUNT_MENU = By.id("navbarAccount");
    private static final By LOGIN_MENU_ITEM = By.id("navbarLoginButton");

    private static final By WELCOME_BANNER_CLOSE = By.cssSelector("button[aria-label='Close Welcome Banner']");
    private static final By COOKIE_BANNER_DISMISS = By.cssSelector("a[aria-label='dismiss cookie message']");
    private static final By OVERLAY_BACKDROP = By.cssSelector(".cdk-overlay-backdrop");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage waitUntilLoaded() {
        waitVisible(SEARCH_BAR);
        return this;
    }

    /**
     * Closes the welcome dialog and the cookie notice if they show up.
     * The welcome dialog is rendered by Angular a moment after load, so we
     * wait for it (briefly) instead of checking the DOM once.
     */
    public HomePage dismissOverlays() {
        clickIfPresent(WELCOME_BANNER_CLOSE, Duration.ofSeconds(5));
        clickIfPresent(COOKIE_BANNER_DISMISS, Duration.ofSeconds(2));
        try {
            new WebDriverWait(driver, TIMEOUT)
                    .until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_BACKDROP));
        } catch (TimeoutException ignored) {
            // Best effort: if a backdrop is stuck, the next click fails with a clear message.
        }
        return this;
    }

    public int productCount() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCTS)).size();
    }

    public LoginPage openLoginPage() {
        safeClick(ACCOUNT_MENU);
        safeClick(LOGIN_MENU_ITEM);
        return new LoginPage(driver).waitUntilDisplayed();
    }

    private void clickIfPresent(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.elementToBeClickable(locator))
                    .click();
        } catch (TimeoutException ignored) {
            // Not shown in this session; nothing to dismiss.
        }
    }
}

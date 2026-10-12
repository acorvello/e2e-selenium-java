package io.github.acorvello.selenium.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Shared helpers for page objects: explicit waits and resilient clicks. */
public abstract class BasePage {

    protected static final Duration TIMEOUT = Duration.ofSeconds(10);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Clicks an element, retrying while an animation or overlay is still
     * in the way. Retrying until the click succeeds is more reliable than
     * a fixed sleep, and unlike a forced JS click it keeps the test honest:
     * if a real user could not click it, the test still fails.
     */
    protected void safeClick(By locator) {
        new WebDriverWait(driver, TIMEOUT)
                .ignoring(ElementClickInterceptedException.class, ElementNotInteractableException.class)
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until(d -> {
                    d.findElement(locator).click();
                    return true;
                });
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
}

package io.github.acorvello.selenium.support;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Base class for all UI tests.
 *
 * Opens a Chrome session on a remote Selenium server (a container in the
 * docker-test-env project) and captures a screenshot when a test fails.
 * The target URLs come from environment variables so the same code runs
 * locally, in Docker and in Jenkins.
 */
public abstract class BaseTest {

    protected static final String BASE_URL = env("BASE_URL", "http://localhost:3001");
    private static final String SELENIUM_URL = env("SELENIUM_URL", "http://localhost:4444/wd/hub");

    protected WebDriver driver;

    // Runs when a test throws, BEFORE @AfterEach quits the browser,
    // so the screenshot still shows the page as it was at the failure.
    @RegisterExtension
    final TestExecutionExceptionHandler screenshotOnFailure = (context, throwable) -> {
        takeScreenshot(context.getRequiredTestMethod().getName());
        throw throwable;
    };

    @BeforeEach
    protected void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        // A large viewport avoids responsive-layout overlaps seen at small sizes.
        options.addArguments("--window-size=1920,1080");
        driver = new RemoteWebDriver(URI.create(SELENIUM_URL).toURL(), options);
        driver.get(BASE_URL);
    }

    @AfterEach
    protected void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testName) {
        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);
            Files.write(dir.resolve(testName + ".png"), png);
        } catch (IOException | RuntimeException e) {
            // Never hide the real test failure because the screenshot failed.
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}

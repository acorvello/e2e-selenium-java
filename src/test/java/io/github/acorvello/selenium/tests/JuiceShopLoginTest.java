package io.github.acorvello.selenium.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.acorvello.selenium.pages.HomePage;
import io.github.acorvello.selenium.pages.LoginPage;
import io.github.acorvello.selenium.support.BaseTest;

class JuiceShopLoginTest extends BaseTest {

    @Test
    void homePageListsProducts() {
        HomePage home = new HomePage(driver).waitUntilLoaded().dismissOverlays();

        assertTrue(home.productCount() > 0, "expected at least one product on the home page");
    }

    @Test
    void loginFormOpensFromAccountMenu() {
        HomePage home = new HomePage(driver).waitUntilLoaded().dismissOverlays();

        LoginPage login = home.openLoginPage();

        // waitUntilDisplayed() already asserts both fields are visible;
        // reaching this line means the login form opened.
        assertTrue(driver.getCurrentUrl().contains("/login"), "expected to be on the login route");
        login.waitUntilDisplayed();
    }

    @Test
    void invalidCredentialsShowAnError() {
        HomePage home = new HomePage(driver).waitUntilLoaded().dismissOverlays();

        String message = home.openLoginPage()
                .loginWith("usuario_invalido@teste.com", "senhaErrada123")
                .errorMessage();

        assertFalse(message.isBlank(), "expected an error message after invalid login");
        assertTrue(message.toLowerCase().contains("invalid"),
                "expected an 'invalid credentials' message but got: " + message);
    }
}

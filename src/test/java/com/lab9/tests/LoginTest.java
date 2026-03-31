package com.lab9.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.lab9.core.BaseTest;

/**
 * Smoke tests for the login page of saucedemo.com.
 * These tests verify the most critical flows and run on both Chrome and Firefox
 * via the CI matrix strategy.
 */
public class LoginTest extends BaseTest {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON   = By.id("login-button");
    private static final By ERROR_MESSAGE  = By.cssSelector("[data-test='error']");
    private static final By PRODUCT_TITLE  = By.cssSelector(".title");

    @Test(groups = "smoke")
    public void smokeLoginPageDisplayed() {
        String title = getDriver().getTitle();
        Assert.assertTrue(title.contains("Swag Labs"),
                "Login page title mismatch. Actual: " + title);
    }

    @Test(groups = "smoke")
    public void smokeLoginSuccess() {
        getDriver().findElement(USERNAME_INPUT).sendKeys("standard_user");
        getDriver().findElement(PASSWORD_INPUT).sendKeys("secret_sauce");
        getDriver().findElement(LOGIN_BUTTON).click();

        String pageTitle = getDriver().findElement(PRODUCT_TITLE).getText();
        Assert.assertEquals(pageTitle, "Products",
                "Should redirect to Products page after successful login.");
    }

    @Test(groups = "smoke")
    public void smokeLoginFail() {
        getDriver().findElement(USERNAME_INPUT).sendKeys("standard_user");
        getDriver().findElement(PASSWORD_INPUT).sendKeys("wrong_password");
        getDriver().findElement(LOGIN_BUTTON).click();

        boolean errorVisible = getDriver().findElement(ERROR_MESSAGE).isDisplayed();
        Assert.assertTrue(errorVisible, "Error message should be displayed on failed login.");
    }
}

package com.lab9.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lab9.core.BaseTest;

/**
 * Demo tests to validate BaseTest setup/teardown and screenshot-on-failure behavior.
 */
public class BaseTestDemoTest extends BaseTest {

    @Test
    public void passTestShouldOpenConfiguredUrl() {
        String title = getDriver().getTitle();
        Assert.assertTrue(title.contains("Swag Labs"), "Title should contain 'Swag Labs'. Actual: " + title);
    }

    @Test
    public void passTestLoginPageLoaded() {
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucedemo.com"), "URL should contain 'saucedemo.com'. Actual: " + currentUrl);
    }

    @Test
    public void failTestWrongTitleAssertion() {
        // Cố tình assert sai để pipeline đỏ và trigger screenshot
        String title = getDriver().getTitle();
        Assert.assertEquals(title, "Wrong Title - This Will Fail",
                "Intentional wrong assertion to demonstrate CI failure and screenshot capture.");
    }
}

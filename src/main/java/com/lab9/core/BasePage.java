package com.lab9.core;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lab9.utils.ConfigReader;

/**
 * Base class for all Page Objects.
 * Provides reusable browser actions with explicit waits.
 */
public class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        Duration waitDuration = Duration.ofSeconds(ConfigReader.getInstance().getExplicitWait());
        this.wait = new WebDriverWait(driver, waitDuration);
    }

    /**
     * Waits until the element is clickable, then performs click.
     * Use this when interacting with controls that may take time to become ready.
     *
     * @param locator locator of the target element
     */
    public void waitAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Waits until the element is visible, clears existing value, then types new text.
     * Use this for input fields to avoid appending text to old values.
     *
     * @param locator locator of the input element
     * @param text text to type into element
     */
    public void waitAndType(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Waits until the element is visible and returns its text content.
     * Use this when validating UI text that may load asynchronously.
     *
     * @param locator locator of the target element
     * @return visible element text
     */
    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /**
     * Checks whether an element is visible on the page.
     * Use this for optional UI checks where a missing/stale element should not fail the test immediately.
     *
     * @param locator locator of the target element
     * @return true if element is visible, otherwise false
     */
    public boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (StaleElementReferenceException ex) {
            return false;
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            return false;
        } catch (org.openqa.selenium.TimeoutException ex) {
            return false;
        }
    }

    /**
     * Scrolls the page until the target element is in view.
     * Use this before interacting with elements outside the viewport.
     *
     * @param locator locator of the element to scroll to
     */
    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /**
     * Waits for the current page to finish loading.
     * Use this right after navigation when subsequent actions depend on full page readiness.
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> "complete".equals(
            ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
        ));
    }

    /**
     * Waits until the element is visible and returns a specific attribute value.
     * Use this for assertions involving dynamic attributes such as value, class, or href.
     *
     * @param locator locator of the target element
     * @param attributeName name of the attribute to read
     * @return attribute value, or null if attribute does not exist
     */
    public String getAttribute(By locator, String attributeName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute(attributeName);
    }
}

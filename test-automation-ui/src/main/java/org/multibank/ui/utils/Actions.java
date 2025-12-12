package org.multibank.ui.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.multibank.core.utils.RetryUtils;
import org.multibank.ui.config.TestConfigLoader;
import org.multibank.ui.constants.TestConstants;

@Slf4j
public class Actions {

    private final int retryCount;
    private final int retryDelay;
    private final Page page;

    public Actions(Page page) {
        this.page = page;
        var config = TestConfigLoader.load();
        this.retryCount = config.retryCount();
        this.retryDelay = config.retryDelayMs();
    }

    public void click(Locator locator) {
        RetryUtils.retryVoid(retryCount, retryDelay, () -> {
            locator.waitFor();
            locator.click();
        });
    }

    public boolean isLocatorVisible(Locator locator) {
        return RetryUtils.retry(retryCount, retryDelay, locator::isVisible);
    }

    public void fill(Locator locator, String value) {
        RetryUtils.retryVoid(retryCount, retryDelay, () -> {
            locator.waitFor();
            locator.fill(value);
        });
    }

    public String getText(Locator locator) {
        return RetryUtils.retry(retryCount, retryDelay, () -> {
            locator.waitFor();
            return locator.textContent().trim();
        });
    }

    public String getAttribute(Locator locator, String attribute) {
        return RetryUtils.retry(retryCount, retryDelay, () -> {
            locator.waitFor();
            return locator.getAttribute(attribute);
        });
    }

    public void scrollIntoView(Locator locator) {
        RetryUtils.retryVoid(retryCount, retryDelay, locator::scrollIntoViewIfNeeded);
    }

    public void waitForVisible(Locator locator) {
        waitForVisible(locator, TestConstants.DEFAULT_TIMEOUT_MS);
    }

    public void waitForVisible(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeoutMs));
    }

    public boolean waitForVisibleSafe(Locator locator, int timeoutMs) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeoutMs));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollContainerBy(Locator container, int scrollAmount) {
        container.evaluate("el => el.scrollTop += " + scrollAmount);
    }

    public void scrollContainerToTop(Locator container) {
        container.evaluate("el => el.scrollTop = 0");
    }
}

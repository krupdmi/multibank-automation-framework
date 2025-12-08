package org.multibank.ui.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.multibank.core.utils.RetryUtils;
import org.multibank.ui.config.TestConfigLoader;

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
}

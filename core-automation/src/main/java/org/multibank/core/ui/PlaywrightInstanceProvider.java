package org.multibank.core.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.multibank.core.enums.BrowserName;

import java.util.List;

import static org.multibank.core.constants.Constants.DEFAULT_TIMEOUT_MS;
import static org.multibank.core.constants.Constants.SLOW_MOTION_MS;

public final class PlaywrightInstanceProvider {

    public static BrowserType.LaunchOptions defaultLaunchOptions() {
        return new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(SLOW_MOTION_MS);
    }

    public static Page createPage(BrowserName browserName, String baseUrl) {
        Playwright playwright = Playwright.create();

        Browser browser;
        switch (browserName) {
            case FIREFOX -> browser = playwright.firefox().launch(defaultLaunchOptions());
            case WEBKIT -> browser = playwright.webkit().launch(defaultLaunchOptions());
            default -> browser = playwright.chromium().launch(defaultLaunchOptions());
        }

        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        if (baseUrl != null && !baseUrl.isBlank()) {
            page.navigate(baseUrl);
        }

        page.setDefaultTimeout(DEFAULT_TIMEOUT_MS);
        return page;
    }
}

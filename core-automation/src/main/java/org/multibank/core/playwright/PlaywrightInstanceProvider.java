package org.multibank.core.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.multibank.core.enums.BrowserName;

import static org.multibank.core.constants.Constants.*;

public final class PlaywrightInstanceProvider {

    public static BrowserType.LaunchOptions defaultLaunchOptions() {
        return new BrowserType.LaunchOptions()
                .setHeadless(DEFAULT_HEADLESS_MODE)
                .setSlowMo(SLOW_MOTION_MS);
    }

    public static PlaywrightSession createPage(BrowserName browserName, String baseUrl) {

        Playwright playwright = Playwright.create();

        Browser browser = switch (browserName) {
            case FIREFOX -> playwright.firefox().launch(defaultLaunchOptions());
            case WEBKIT -> playwright.webkit().launch(defaultLaunchOptions());
            default -> playwright.chromium().launch(defaultLaunchOptions());
        };

        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_SIZE)
                        .setScreenSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_SIZE)
        );

        Page page = context.newPage();
        if (baseUrl != null && !baseUrl.isBlank()) {
            page.navigate(baseUrl);
        }

        page.setDefaultTimeout(DEFAULT_TIMEOUT_MS);

        return new PlaywrightSession(playwright, browser, context, page);
    }
}

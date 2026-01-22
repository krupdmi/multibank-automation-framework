package org.multibank.core.playwright;

import com.microsoft.playwright.*;
import org.multibank.core.enums.BrowserName;

import static org.multibank.core.constants.Constants.*;

public final class PlaywrightInstanceProvider {

    public static BrowserType.LaunchOptions defaultLaunchOptions(boolean headless) {
        return new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(SLOW_MOTION_MS);
    }

    public static PlaywrightSession createPage(BrowserName browserName, String baseUrl, boolean isMobile, boolean headless) {
        Playwright playwright = Playwright.create();

        Browser browser = switch (browserName) {
            case FIREFOX -> playwright.firefox().launch(defaultLaunchOptions(headless));
            case WEBKIT -> playwright.webkit().launch(defaultLaunchOptions(headless));
            default -> playwright.chromium().launch(defaultLaunchOptions(headless));
        };

        int width = isMobile ? MOBILE_WIDTH : DESKTOP_WIDTH;
        int height = isMobile ? MOBILE_HEIGHT : DESKTOP_HEIGHT;

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(width, height)
                .setScreenSize(width, height);

        if (isMobile) {
            contextOptions
                    .setHasTouch(true)
                    .setIsMobile(true);
        }

        BrowserContext context = browser.newContext(contextOptions);
        Page page = context.newPage();

        if (baseUrl != null && !baseUrl.isBlank()) {
            page.navigate(baseUrl);
        }

        page.setDefaultTimeout(DEFAULT_TIMEOUT_MS);

        return new PlaywrightSession(playwright, browser, context, page);
    }
}
package org.multibank.core.playwright;

import com.microsoft.playwright.*;

public final class PlaywrightSession {

    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;
    private final Page page;

    public PlaywrightSession(Playwright playwright, Browser browser, BrowserContext context, Page page) {
        this.playwright = playwright;
        this.browser = browser;
        this.context = context;
        this.page = page;
    }

    public Page page() {
        return page;
    }

    public BrowserContext context() {
        return context;
    }

    public Browser browser() {
        return browser;
    }

    public Playwright playwright() {
        return playwright;
    }

    public void close() {
        try { context.close(); } catch (Exception ignored) {}
        try { browser.close(); } catch (Exception ignored) {}
        try { playwright.close(); } catch (Exception ignored) {}
    }
}

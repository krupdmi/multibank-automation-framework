package org.multibank.ui.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class Actions {

    private final Page page;

    public Actions(Page page) {
        this.page = page;
    }

    public boolean isLocatorVisible(Locator locator) {
        try {
            return locator.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void click(Locator locator) {
        locator.click();
    }
}

package org.multibank.ui.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class WaitActions {

    private final Page page;

    public WaitActions(Page page) {
        this.page = page;
    }

    public void waitForVisible(String selector) {
        page.waitForSelector(selector,
            new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
        );
    }
}

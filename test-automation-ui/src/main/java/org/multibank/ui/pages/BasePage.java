package org.multibank.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.multibank.ui.utils.Actions;

public abstract class BasePage {

    protected final Page page;
    protected final Actions actions;

    protected BasePage(Page page) {
        this.page = page;
        this.actions = new Actions(page);
    }

    protected Locator locator(String selector) {
        return page.locator(selector);
    }

}
package org.multibank.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class HeaderPage extends BasePage {

    private final Locator header;

    public HeaderPage(Page page) {
        super(page);
        this.header = locator("[class*='header-main-content']");
    }

    private Locator topNavigationItem(String label) {
        return header.getByRole(
                        AriaRole.LINK,
                        new Locator.GetByRoleOptions().setName(label))
                .or(header.getByText(label));
    }

    private Locator dropdownPanel() {
        // Trade
        Locator headless = locator("[id*='popover-panel'][data-headlessui-state='open']");
        if (headless.count() > 0) {
            return headless.first();
        }

        // More, Features, Support, About Us
        Locator classPanel = locator("div[class*='popover-panel']");
        if (classPanel.count() > 0) {
            return classPanel.first();
        }

        // Default
        Locator fallback = locator("div:has(a[href])").filter(
                new Locator.FilterOptions().setHasText("")
        );

        return fallback.first();
    }

    private Locator dropdownItem(String label) {
        Locator panel = dropdownPanel();

        Locator byRole = panel.getByRole(
                AriaRole.LINK,
                new Locator.GetByRoleOptions().setName(label)
        );
        if (byRole.count() > 0) return byRole.first();

        return panel.getByText(label);
    }

    @Step("Is top nav item '{label}' visible?")
    public boolean isTopNavItemVisible(String label) {
        return actions.isLocatorVisible(topNavigationItem(label));
    }

    @Step("Click top menu item '{label}'")
    public void clickTopNavItem(String label) throws InterruptedException {
        actions.click(topNavigationItem(label));
    }

    @Step("Open dropdown '{label}'")
    public void openDropdown(String label) throws InterruptedException {
        actions.click(topNavigationItem(label));
        dropdownPanel().waitFor();
    }

    @Step("Is dropdown item '{label}' visible in menu?")
    public boolean isDropdownItemVisible(String label) {
        return actions.isLocatorVisible(dropdownItem(label));
    }
}

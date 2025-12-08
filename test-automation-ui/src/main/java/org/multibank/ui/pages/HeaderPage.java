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
        return locator("[id*='popover-panel'][data-headlessui-state='open']");
    }

    private Locator dropdownItem(String label) {
        return dropdownPanel().getByRole(
                AriaRole.LINK,
                new Locator.GetByRoleOptions().setName(label)
        );
    }

    @Step("Is top nav item '{label}' visible?")
    public boolean isTopNavItemVisible(String label) {
        return actions.isLocatorVisible(topNavigationItem(label));
    }

    @Step("Click top menu item '{label}'")
    public void clickTopNavItem(String label) {
        actions.click(topNavigationItem(label));
    }

    @Step("Open dropdown '{label}'")
    public void openDropdown(String label) {
        actions.click(topNavigationItem(label));
        dropdownPanel().waitFor();
    }

    @Step("Is dropdown item '{label}' visible in '{menu}'?")
    public boolean isDropdownItemVisible(String menu, String label) {
        return actions.isLocatorVisible(dropdownItem(label));
    }
}

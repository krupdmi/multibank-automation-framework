package org.multibank.ui.pages.home;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.multibank.ui.pages.BasePage;

public class HeaderPage extends BasePage {

    private final Locator header;
    private final Locator userActionsContainer;
    private final Locator languagePickerButton;

    public HeaderPage(Page page) {
        super(page);
        this.header = locator("[class*='header-main-content']");
        this.userActionsContainer = locator("[class*='user-actions']");
        this.languagePickerButton = locator("#language-header-option-open-button");
    }

    private Locator topNavigationItem(String label) {
        return header.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(label)).or(header.getByText(label));
    }

    private Locator dropdownPanel() {
        Locator openPanel = locator("div[class*='popover-panel'][data-headlessui-state='open']");
        if (openPanel.count() > 0) {
            return openPanel.first();
        }

        Locator visiblePanel = locator("div[class*='popover-panel']")
                .filter(new Locator.FilterOptions().setHas(locator("a")));
        if (visiblePanel.count() > 0) {
            return visiblePanel.first();
        }

        return locator("[id*='popover-panel']").first();
    }

    private Locator dropdownItem(String label) {
        Locator panel = dropdownPanel();

        Locator filterByTextDiv = panel.locator("a")
                .filter(new Locator.FilterOptions().setHas(locator("div[class*='text']").filter(new Locator.FilterOptions().setHasText(label))));
        if (filterByTextDiv.count() > 0) {
            return filterByTextDiv.first();
        }

        Locator filterByText = panel.locator("a")
                .filter(new Locator.FilterOptions().setHasText(label));
        if (filterByText.count() > 0) {
            return filterByText.first();
        }

        return panel.getByText(label);
    }

    private Locator userActionButton(String label) {
        Locator filterBySpan = userActionsContainer.locator("a")
                .filter(new Locator.FilterOptions().setHas(locator("span").filter(new Locator.FilterOptions().setHasText(label))));
        if (filterBySpan.count() > 0) {
            return filterBySpan.first();
        }

        return userActionsContainer.locator("a").filter(new Locator.FilterOptions().setHasText(label));
    }

    private Locator languagePanel() {
        return locator("[id*='popover-panel']:not([hidden])").or(locator("div[class*='popover-panel']")).first();
    }

    private Locator languageOption(String languageLabel) {
        return languagePanel().locator("a[class*='language-list-item']").filter(new Locator.FilterOptions().setHasText(languageLabel));
    }

    @Step("Is top menu item '{label}' visible?")
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

    @Step("Is dropdown item '{label}' visible in menu?")
    public boolean isDropdownItemVisible(String label) {
        return actions.isLocatorVisible(dropdownItem(label));
    }

    @Step("Click dropdown item '{label}'")
    public void clickDropdownItem(String label) {
        actions.click(dropdownItem(label));
    }

    @Step("Is user action button '{label}' visible?")
    public boolean isUserActionVisible(String label) {
        return actions.isLocatorVisible(userActionButton(label));
    }

    @Step("Click user action button '{label}'")
    public void clickUserAction(String label) {
        actions.click(userActionButton(label));
    }

    @Step("Open language picker")
    public void openLanguagePicker() {
        actions.click(languagePickerButton);
        languagePanel().waitFor();
    }

    @Step("Select language '{languageLabel}'")
    public void selectLanguage(String languageLabel) {
        openLanguagePicker();
        actions.click(languageOption(languageLabel));

        page.waitForLoadState();
        header.waitFor();
    }

    public String getDropdownItemHref(String label) {
        return hrefOf(dropdownItem(label));
    }

    public String getTopNavigationItemHref(String label) {
        return hrefOf(topNavigationItem(label));
    }

    public String getUserActionHref(String label) {
        return hrefOf(userActionButton(label));
    }

    private String hrefOf(Locator locator) {
        try {
            return locator.getAttribute("href");
        } catch (Exception e) {
            return null;
        }
    }
}

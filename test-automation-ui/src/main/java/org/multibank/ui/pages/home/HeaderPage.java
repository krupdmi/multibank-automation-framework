package org.multibank.ui.pages.home;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.multibank.ui.pages.BasePage;

public class HeaderPage extends BasePage {

    // main menu locators
    private static final String HEADER_MENU = "[class*='header-main-content']";
    private static final String USER_ACTIONS_MENU = "[class*='user-actions']";

    // language locators
    private static final String LANGUAGE_SELECTION_BUTTON = "#language-header-option-open-button";
    private static final String LANGUAGE_LIST_ITEM = "a[class*='language-list-item']";

    // dropdown locators
    private static final String POPOVER_PANEL = "div[class*='popover-panel']";
    private static final String POPOVER_PANEL_OPEN = "div[class*='popover-panel'][data-headlessui-state='open']";
    private static final String POPOVER_PANEL_BY_ID = "[id*='popover-panel']";
    private static final String POPOVER_PANEL_NOT_HIDDEN = "[id*='popover-panel']:not([hidden])";

    // element locators
    private static final String TEXT_DIV = "div[class*='text']";
    private static final String LINK = "a";
    private static final String SPAN = "span";
    private static final String LINK_BY_HREF_TEMPLATE = "a[href='%s']";
    private static final String HREF = "href";

    private final Locator header;
    private final Locator userActionsContainer;
    private final Locator languagePickerButton;

    public HeaderPage(Page page) {
        super(page);
        this.header = locator(HEADER_MENU);
        this.userActionsContainer = locator(USER_ACTIONS_MENU);
        this.languagePickerButton = locator(LANGUAGE_SELECTION_BUTTON);
    }

    private Locator topNavigationItem(String label) {
        return header.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(label))
                .or(header.getByText(label));
    }

    private Locator dropdownPanel() {
        Locator openPanel = locator(POPOVER_PANEL_OPEN);
        if (openPanel.count() > 0) {
            return openPanel.first();
        }

        Locator visiblePanel = locator(POPOVER_PANEL).
                filter(new Locator.FilterOptions().setHas(locator(LINK)));
        if (visiblePanel.count() > 0) {
            return visiblePanel.first();
        }

        return locator(POPOVER_PANEL_BY_ID).first();
    }

    private Locator dropdownItem(String label) {
        Locator panel = dropdownPanel();

        Locator filterByTextDiv = panel.locator(LINK)
                .filter(new Locator.FilterOptions().setHas(locator(TEXT_DIV).filter(new Locator.FilterOptions().setHasText(label))));
        if (filterByTextDiv.count() > 0) {
            return filterByTextDiv.first();
        }

        Locator filterByText = panel.locator(LINK).
                filter(new Locator.FilterOptions().setHasText(label));
        if (filterByText.count() > 0) {
            return filterByText.first();
        }

        return panel.getByText(label);
    }

    private Locator userActionButton(String label) {
        Locator filterBySpan = userActionsContainer.locator(LINK)
                .filter(new Locator.FilterOptions().setHas(locator(SPAN).filter(new Locator.FilterOptions().setHasText(label))));
        if (filterBySpan.count() > 0) {
            return filterBySpan.first();
        }

        return userActionsContainer.locator(LINK).filter(new Locator.FilterOptions().setHasText(label));
    }

    private Locator languagePanel() {
        return locator(POPOVER_PANEL_NOT_HIDDEN).or(locator(POPOVER_PANEL)).first();
    }

    private Locator languageOption(String languageLabel) {
        return languagePanel().locator(LANGUAGE_LIST_ITEM).filter(new Locator.FilterOptions().setHasText(languageLabel));
    }

    @Step("Is top menu item '{label}' visible?")
    public boolean isTopNavItemVisible(String label) {
        return actions.isLocatorVisible(topNavigationItem(label));
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

    @Step("Open language picker")
    public void openLanguagePicker() {
        actions.click(languagePickerButton);
        languagePanel().waitFor();
    }

    @Step("Select language '{languageLabel}'")
    public void selectLanguage(String languageLabel) {
        openLanguagePicker();
        String targetHref = languageOption(languageLabel).getAttribute(HREF);
        actions.click(languageOption(languageLabel));

        page.waitForLoadState();
        page.locator(String.format(LINK_BY_HREF_TEMPLATE, targetHref)).first().waitFor();
        header.waitFor();
    }

    public String getDropdownItemHref(String label) {
        return getHref(dropdownItem(label));
    }

    public String getTopNavigationItemHref(String label) {
        return getHref(topNavigationItem(label));
    }

    public String getUserActionHref(String label) {
        return getHref(userActionButton(label));
    }

    private String getHref(Locator locator) {
        try {
            return locator.getAttribute(HREF);
        } catch (Exception e) {
            return null;
        }
    }
}

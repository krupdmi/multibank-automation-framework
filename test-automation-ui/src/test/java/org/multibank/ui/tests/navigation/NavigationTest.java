package org.multibank.ui.tests.navigation;

import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.multibank.ui.assertions.Assertions;
import org.multibank.ui.base.BaseUiTest;
import org.multibank.ui.dataprovider.NavigationDataProvider;
import org.multibank.ui.models.LanguageNavigation;
import org.multibank.ui.models.NavigationItem;
import org.multibank.ui.pages.home.HeaderPage;

import java.util.List;

@Epic("Navigation & Layout")
@Feature("Top header navigation")
@Tag("Regression")
@Slf4j
class NavigationTest extends BaseUiTest {

    private HeaderPage header;
    private LanguageNavigation navData;

    @BeforeEach
    void setUpNavigation() {
        header = new HeaderPage(page());
        navData = NavigationDataProvider.loadForConfiguredLanguage();

        log.info("Selecting language: {}", navData.getLanguageLabel());
        header.selectLanguage(navData.getLanguageLabel());
    }

    @Test
    @DisplayName("Top navigation menu displays correctly")
    @Story("User sees main navigation items")
    void topNavigationMenuShouldListMainMenuItems() {
        List<NavigationItem> menuItems = navData.getTopNavigationPanel().getItems();

        log.info("Testing navigation for language: {}", navData.getLanguageLabel());
        log.info("Expected menu items: {}", menuItems.stream().map(NavigationItem::getLabel).toList());

        for (NavigationItem item : menuItems) {
            String label = item.getLabel();
            log.info("Validate menu item: {}", label);

            Assertions.expectVisible(label, header.isTopNavItemVisible(label));

            String href = header.getTopNavigationItemHref(label);

            if (item.hasSubItem()) {
                Assertions.expectNull(
                        "Menu item '" + label + "' with subItem should not have direct link", href
                );
            } else {
                Assertions.expectNotNull(
                        "Menu item '" + label + "' without subItem should have link", href
                );
            }
        }

        Assertions.assertAll();
        log.info("All menu items are present and valid");
    }

    @Test
    @DisplayName("Top navigation menu displays dropdown items")
    @Story("User expands dropdown menus")
    void dropdownMenusShouldListExpectedItems() {
        List<NavigationItem> menuItems = navData.getTopNavigationPanel().getItems();

        log.info("Testing dropdowns for language: {}", navData.getLanguageLabel());

        for (NavigationItem parentItem : menuItems) {
            if (!parentItem.hasSubItem()) {
                continue;
            }

            String parentLabel = parentItem.getLabel();
            log.info("Opening dropdown: {}", parentLabel);
            header.openDropdown(parentLabel);

            for (NavigationItem childItem : parentItem.getSubItem()) {
                String childLabel = childItem.getLabel();
                log.info("Validate dropdown item '{}' in menu '{}'", childLabel, parentLabel);

                Assertions.expectVisible(
                        "Dropdown item '" + childLabel + "' in menu '" + parentLabel + "'",
                        header.isDropdownItemVisible(childLabel)
                );

                String href = header.getDropdownItemHref(childLabel);
                Assertions.expectNotNull(
                        "Dropdown item '" + childLabel + "' should have a link",
                        href
                );
            }
        }

        Assertions.assertAll();
    }

    @Test
    @DisplayName("User action buttons are visible")
    @Story("User sees login and signup options")
    void userActionButtonsShouldBeVisible() {
        List<NavigationItem> userActions = navData.getUserAction().getItems();

        log.info("Testing user action buttons for language: {}", navData.getLanguageLabel());

        for (NavigationItem action : userActions) {
            String label = action.getLabel();
            log.info("Validate user action button: {}", label);

            Assertions.expectVisible(label, header.isUserActionVisible(label));

            if (action.getLink() != null) {
                String href = header.getUserActionHref(label);
                Assertions.expectNotNull(
                        "User action '" + label + "' should have a link",
                        href
                );
            }
        }

        Assertions.assertAll();
        log.info("All user action buttons are present and valid");
    }
}

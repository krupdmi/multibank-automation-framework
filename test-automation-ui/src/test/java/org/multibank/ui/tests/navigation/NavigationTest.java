package org.multibank.ui.tests.navigation;

import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.multibank.ui.assertions.Assertions;
import org.multibank.ui.base.BaseUiTest;
import org.multibank.ui.dataprovider.NavigationDataProvider;
import org.multibank.ui.pages.home.HeaderPage;

import java.util.List;
import java.util.Map;

@Epic("Navigation & Layout")
@Feature("Top header navigation")
@Tag("Regression")
@Slf4j
class NavigationTest extends BaseUiTest {

    @Test
    @DisplayName("Top navigation menu displays correctly")
    @Story("User sees main navigation items")
    void topNavigationMenuShouldListMainMenuItems() {
        HeaderPage header = new HeaderPage(page());

        List<String> expectedMenuItems = NavigationDataProvider
                .loadNavigationItemsData()
                .getTopNavigationItems();

        Map<String, List<String>> dropdownItems =
                NavigationDataProvider.loadNavigationItemsData().getDropdowns();

        log.info("Expected menu items: {}", expectedMenuItems);

        expectedMenuItems.forEach(item -> {

            log.info("Validate menu item: {}", item);

            Assertions.expectVisible(item, header.isTopNavItemVisible(item));

            boolean hasDropdown = dropdownItems.containsKey(item);
            String href = header.getTopNavigationItemHref(item);

            if (hasDropdown) {
                Assertions.expectNull(
                        "Menu item '" + item + "' with a dropdown should not have link", href
                );
            } else {
                Assertions.expectNotNull(
                        "Menu item '" + item + "' without a dropdown should have link", href
                );
            }
        });

        Assertions.assertAll();

        log.info("All menu items are present and valid");
    }

    @Test
    @DisplayName("Top navigation menu displays dropdown items")
    @Story("User expands dropdown menus")
    void dropdownMenusShouldListExpectedItems() {
        HeaderPage header = new HeaderPage(page());

        Map<String, List<String>> expectedDropdowns = NavigationDataProvider
                .loadNavigationItemsData()
                .getDropdowns();

        log.info("Expected drop down menu items: {}", expectedDropdowns);

        expectedDropdowns.forEach((menu, items) -> {
            header.openDropdown(menu);

            items.forEach(item -> {
                log.info("Validate dropdown item '{}' in menu '{}'", item, menu);

                Assertions.expectVisible(
                        "Dropdown item '" + item + "' in menu '" + menu + "'",
                        header.isDropdownItemVisible(item)
                );

                String href = header.getDropdownItemHref(item);
                Assertions.expectNotNull(
                        "Dropdown item '" + item + "' should have a link",
                        href
                );
            });
        });

        Assertions.assertAll();
    }

}


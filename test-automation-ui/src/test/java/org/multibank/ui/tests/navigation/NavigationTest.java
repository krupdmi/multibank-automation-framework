package org.multibank.ui.tests.navigation;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.multibank.ui.assertions.Assertions;
import org.multibank.ui.base.BaseUiTest;
import org.multibank.ui.dataprovider.NavigationDataProvider;
import org.multibank.ui.pages.HeaderPage;

import java.util.List;
import java.util.Map;

@Epic("Navigation & Layout")
@Feature("Top header navigation")
@Tag("regression")
class NavigationTest extends BaseUiTest {

    @Test
    @DisplayName("Top navigation menu displays correctly")
    @Story("User sees main navigation items")
    void topNavigationMenuShouldExposePrimaryEntryPoints() {
        HeaderPage header = new HeaderPage(page());

        List<String> expectedMenuItems = NavigationDataProvider.loadNavigationItemsData().getTopNavigationItems();

        expectedMenuItems.forEach(item ->
                                          Assertions.expectVisible(item, header.isTopNavItemVisible(item))
        );

        Assertions.assertAll();
    }

    @Test
    @DisplayName("Top navigation menu displays dropdown items")
    @Story("User expands dropdown menus")
    void dropdownMenusShouldListExpectedItems() {
        HeaderPage header = new HeaderPage(page());

        Map<String, List<String>> expectedDropdowns = NavigationDataProvider.loadNavigationItemsData().getDropdowns();

        expectedDropdowns.forEach((menu, items) -> {
            items.forEach(item ->
                                  Assertions.expectVisible(
                                          "Dropdown item '" + item + "' in menu '" + menu + "'",
                                          header.isDropdownItemVisible(menu, item)
                                  )
            );
        });

        Assertions.assertAll();
    }
}

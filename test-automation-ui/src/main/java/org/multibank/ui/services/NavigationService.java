package org.multibank.ui.services;

import com.microsoft.playwright.Page;
import org.multibank.ui.pages.HeaderPage;

public class NavigationService {

    private final HeaderPage headerPage;

    public NavigationService(Page page) {
        this.headerPage = new HeaderPage(page);
    }

    public boolean isTopItemVisible(String label) {
        return headerPage.isTopNavItemVisible(label);
    }

    public boolean isDropdownItemVisible(String menu, String item) {
        return headerPage.isDropdownItemVisible(menu, item);
    }

    public void openMenu(String label) {
        headerPage.clickTopNavItem(label);
    }
}

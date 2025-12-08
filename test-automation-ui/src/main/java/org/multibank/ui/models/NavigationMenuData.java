package org.multibank.ui.models;

import java.util.List;
import java.util.Map;

public class NavigationMenuData {

    private List<String> topNavigationItems;
    private List<String> userMenuItems;
    private Map<String, List<String>> dropdowns;

    public List<String> getTopNavigationItems() {
        return topNavigationItems;
    }

    public void setTopNavigationItems(List<String> topNavigationItems) {
        this.topNavigationItems = topNavigationItems;
    }

    public List<String> getUserMenuItems() {
        return userMenuItems;
    }

    public void setUserMenuItems(List<String> userMenuItems) {
        this.userMenuItems = userMenuItems;
    }

    public Map<String, List<String>> getDropdowns() {
        return dropdowns;
    }

    public void setDropdowns(Map<String, List<String>> dropdowns) {
        this.dropdowns = dropdowns;
    }
}

package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.multibank.ui.models.NavigationMenuData;

import java.io.IOException;
import java.io.InputStream;

public final class NavigationDataProvider {

    private static final String NAV_DATA_PATH = "/testdata/navigation-menu-items.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static NavigationMenuData loadNavigationItemsData() {
        try (InputStream is = NavigationDataProvider.class.getResourceAsStream(NAV_DATA_PATH)) {
            if (is == null) {
                throw new IllegalStateException("Navigation JSON not found on classpath: " + NAV_DATA_PATH);
            }
            return MAPPER.readValue(is, NavigationMenuData.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load navigation data from " + NAV_DATA_PATH, e);
        }
    }
}

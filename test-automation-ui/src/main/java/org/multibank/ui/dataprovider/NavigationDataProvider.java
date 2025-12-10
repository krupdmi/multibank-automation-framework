package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.models.NavigationMenuData;

import java.io.InputStream;

@Slf4j
public final class NavigationDataProvider {

    private static final String DATA_PATH = "/testdata/navigation-menu-items.json";
    private static NavigationMenuData cachedData;

    public static synchronized NavigationMenuData loadNavigationItemsData() {
        if (cachedData != null) {
            return cachedData;
        }

        log.info("Loading navigation menu data from {}", DATA_PATH);
        try (InputStream is = NavigationDataProvider.class.getResourceAsStream(DATA_PATH)) {
            if (is == null) {
                throw new RuntimeException("Navigation menu data file not found: " + DATA_PATH);
            }

            ObjectMapper mapper = new ObjectMapper();
            cachedData = mapper.readValue(is, NavigationMenuData.class);

            log.info("Navigation menu data loaded successfully");
            return cachedData;

        } catch (Exception e) {
            log.error("Failed to load navigation menu data", e);
            throw new RuntimeException("Failed to load navigation menu data from " + DATA_PATH, e);
        }
    }
}

package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.config.TestConfigLoader;
import org.multibank.ui.models.LanguageNavigation;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class NavigationDataProvider {

    private static final String DATA_PATH_TEMPLATE = "/testdata/%s/navigation-menu-items.json";
    private static final Map<String, LanguageNavigation> cache = new ConcurrentHashMap<>();

    public static LanguageNavigation loadForLanguage(String languageCode) {
        return cache.computeIfAbsent(languageCode, NavigationDataProvider::loadFromFile);
    }

    public static LanguageNavigation loadForConfiguredLanguage() {
        String lang = TestConfigLoader.load().lang();
        return loadForLanguage(lang);
    }

    private static LanguageNavigation loadFromFile(String languageCode) {
        String dataPath = String.format(DATA_PATH_TEMPLATE, languageCode);
        log.info("Loading navigation menu data from {}", dataPath);

        try (InputStream is = NavigationDataProvider.class.getResourceAsStream(dataPath)) {
            if (is == null) {
                throw new RuntimeException("Navigation menu data file not found: " + dataPath);
            }

            ObjectMapper mapper = new ObjectMapper();
            LanguageNavigation navigationData = mapper.readValue(is, LanguageNavigation.class);

            log.info("Navigation menu data loaded successfully for language: {}", languageCode);
            return navigationData;

        } catch (Exception e) {
            log.error("Failed to load navigation menu data for language: {}", languageCode, e);
            throw new RuntimeException("Failed to load navigation menu data from " + dataPath, e);
        }
    }
}

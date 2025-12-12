package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.config.TestConfigLoader;
import org.multibank.ui.models.SpotTradingData;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpotTradingDataProvider {

    private static final String DATA_PATH_TEMPLATE = "/testdata/%s/spot-trading.json";
    private static final Map<String, SpotTradingData> cache = new ConcurrentHashMap<>();

    public static SpotTradingData loadForLanguage(String languageCode) {
        return cache.computeIfAbsent(languageCode, SpotTradingDataProvider::loadFromFile);
    }

    public static SpotTradingData loadForConfiguredLanguage() {
        String lang = TestConfigLoader.load().lang();
        return loadForLanguage(lang);
    }

    private static SpotTradingData loadFromFile(String languageCode) {
        String dataPath = String.format(DATA_PATH_TEMPLATE, languageCode);
        log.info("Loading spot trading data from {}", dataPath);

        try (InputStream is = SpotTradingDataProvider.class.getResourceAsStream(dataPath)) {
            if (is == null) {
                throw new RuntimeException("Spot trading data file not found: " + dataPath);
            }

            ObjectMapper mapper = new ObjectMapper();
            SpotTradingData data = mapper.readValue(is, SpotTradingData.class);

            log.info("Spot trading data loaded successfully for language: {}", languageCode);
            return data;

        } catch (Exception e) {
            log.error("Failed to load spot trading data for language: {}", languageCode, e);
            throw new RuntimeException("Failed to load spot trading data from " + dataPath, e);
        }
    }
}

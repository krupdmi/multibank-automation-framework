package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.models.SpotTradingData;

import java.io.InputStream;

@Slf4j
public class SpotTradingDataProvider {

    private static final String DATA_PATH = "/testdata/spot-trading.json";
    private static SpotTradingData cachedData;

    public static synchronized SpotTradingData loadSpotTradingData() {
        if (cachedData != null) {
            return cachedData;
        }

        log.info("Loading spot trading data from {}", DATA_PATH);
        try (InputStream is = SpotTradingDataProvider.class.getResourceAsStream(DATA_PATH)) {
            if (is == null) {
                throw new RuntimeException("Spot trading data file not found: " + DATA_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            cachedData = mapper.readValue(is, SpotTradingData.class);
            log.info("Spot trading data was loaded successfully");
            return cachedData;
        } catch (Exception e) {
            log.error("Failed to load spot trading data", e);
            throw new RuntimeException("Failed to load spot trading data from " + DATA_PATH, e);
        }
    }
}
package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.config.TestConfigLoader;
import org.multibank.ui.models.ContentValidationData;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ContentValidationDataProvider {

    private static final String DATA_PATH_TEMPLATE = "/testdata/%s/content-validation.json";
    private static final Map<String, ContentValidationData> cache = new ConcurrentHashMap<>();

    public static ContentValidationData loadForLanguage(String languageCode) {
        return cache.computeIfAbsent(languageCode, ContentValidationDataProvider::loadFromFile);
    }

    public static ContentValidationData loadForConfiguredLanguage() {
        String lang = TestConfigLoader.load().lang();
        return loadForLanguage(lang);
    }

    private static ContentValidationData loadFromFile(String languageCode) {
        String dataPath = String.format(DATA_PATH_TEMPLATE, languageCode);
        log.info("Loading content validation data from {}", dataPath);

        try (InputStream is = ContentValidationDataProvider.class.getResourceAsStream(dataPath)) {
            if (is == null) {
                throw new RuntimeException("Content validation data file not found: " + dataPath);
            }

            ObjectMapper mapper = new ObjectMapper();
            ContentValidationData data = mapper.readValue(is, ContentValidationData.class);

            log.info("Content validation data loaded successfully for language: {}", languageCode);
            return data;

        } catch (Exception e) {
            log.error("Failed to load content validation data for language: {}", languageCode, e);
            throw new RuntimeException("Failed to load content validation data from " + dataPath, e);
        }
    }
}

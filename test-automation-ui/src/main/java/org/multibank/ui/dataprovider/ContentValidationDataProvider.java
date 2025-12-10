package org.multibank.ui.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.models.ContentValidationData;

import java.io.InputStream;

@Slf4j
public class ContentValidationDataProvider {

    private static final String DATA_PATH = "/testdata/content-validation.json";
    private static ContentValidationData cachedData;

    public static synchronized ContentValidationData loadContentValidationData() {
        if (cachedData != null) {
            return cachedData;
        }

        log.info("Loading content validation data from {}", DATA_PATH);
        try (InputStream is = ContentValidationDataProvider.class.getResourceAsStream(DATA_PATH)) {
            if (is == null) {
                throw new RuntimeException("Content validation data file not found: " + DATA_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            cachedData = mapper.readValue(is, ContentValidationData.class);
            log.info("Content validation data was loaded successfully");
            return cachedData;
        } catch (Exception e) {
            log.error("Failed to load content validation data", e);
            throw new RuntimeException("Failed to load content validation data from " + DATA_PATH, e);
        }
    }
}
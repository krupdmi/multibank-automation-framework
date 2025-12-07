package org.multibank.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class TestConfigLoader {

    private static final String CONFIG_PATH = "/config/ui-config.json";

    public static TestConfig load() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = TestConfigLoader.class.getResourceAsStream(CONFIG_PATH)) {
            if (is == null) {
                throw new IllegalStateException("Cannot find config file: " + CONFIG_PATH);
            }
            return mapper.readValue(is, TestConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load UI config from " + CONFIG_PATH, e);
        }
    }
}

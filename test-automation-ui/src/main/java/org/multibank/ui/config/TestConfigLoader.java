package org.multibank.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public final class TestConfigLoader {

    private static TestConfig cached;
    private static final String CONFIG_PATH = "/config/ui-config.json";

    public static synchronized TestConfig load() {
        if (cached == null) {
            cached = loadFromFile();
            cached = applyOverrides(cached);
        }
        return cached;
    }

    private static TestConfig loadFromFile() {
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

    private static TestConfig applyOverrides(TestConfig config) {
        return new TestConfig(
                override("base.url", "BASE_URL", config.baseUrl()),
                override("browser", "BROWSER", config.browser()),
                override("environment", "ENVIRONMENT", config.environment()),
                override("execution", "EXECUTION", config.execution()),
                override("lang", "LANG", config.lang()),
                override("platform", "PLATFORM", config.platform()),
                Boolean.parseBoolean(override("headless", "HEADLESS", String.valueOf(config.headless()))),
                Integer.parseInt(override("retry.count", "RETRY_COUNT", String.valueOf(config.retryCount()))),
                Integer.parseInt(override("retry.delay.ms", "RETRY_DELAY_MS", String.valueOf(config.retryDelayMs())))
        );
    }

    private static String override(String sysProp, String envVar, String defaultValue) {
        String value = System.getProperty(sysProp);
        if (value != null && !value.isBlank()) return value;

        value = System.getenv(envVar);
        if (value != null && !value.isBlank()) return value;

        return defaultValue;
    }
}
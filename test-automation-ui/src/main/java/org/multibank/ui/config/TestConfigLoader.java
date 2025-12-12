package org.multibank.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public final class TestConfigLoader {

    private static TestConfig cached;
    private static final String CONFIG_PATH = "/config/ui-config.json";
    private static final String DEFAULT_LANG = "en";

    public static synchronized TestConfig load() {
        if (cached == null) {
            cached = loadConfig();
            logTestConfiguration(cached);
        }
        return cached;
    }

    private static TestConfig loadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        TestConfig fileConfig;

        try (InputStream is = TestConfigLoader.class.getResourceAsStream(CONFIG_PATH)) {
            if (is == null) {
                throw new IllegalStateException("Cannot find config file: " + CONFIG_PATH);
            }
            fileConfig = mapper.readValue(is, TestConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load UI config from " + CONFIG_PATH, e);
        }

        String baseUrl = getPropertyOrDefault("base.url", "BASE_URL", fileConfig.baseUrl());
        String browser = getPropertyOrDefault("browser", "BROWSER", fileConfig.browser());
        String environment = getPropertyOrDefault("environment", "ENVIRONMENT", fileConfig.environment());
        String execution = getPropertyOrDefault("execution", "EXECUTION", fileConfig.execution());
        String lang = getPropertyOrDefault("lang", "LANG", fileConfig.lang() != null ? fileConfig.lang() : DEFAULT_LANG);

        int retryCount = parseIntValue(
                getPropertyOrDefault("retry.count", "RETRY_COUNT", String.valueOf(fileConfig.retryCount())),
                fileConfig.retryCount(),
                "retry.count"
        );

        int retryDelayMs = parseIntValue(
                getPropertyOrDefault("retry.delay.ms", "RETRY_DELAY_MS", String.valueOf(fileConfig.retryDelayMs())),
                fileConfig.retryDelayMs(),
                "retry.delay.ms"
        );

        return new TestConfig(
                baseUrl,
                browser,
                environment,
                execution,
                lang,
                retryCount,
                retryDelayMs
        );
    }

    private static String getPropertyOrDefault(String systemPropertyKey, String envKey, String defaultValue) {
        String systemProp = System.getProperty(systemPropertyKey);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp;
        }

        String envVar = System.getenv(envKey);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }

        return defaultValue;
    }

    private static int parseIntValue(String value, int defaultValue, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("Invalid integer value for {}: '{}'. Using default: {}", fieldName, value, defaultValue);
            return defaultValue;
        }
    }

    private static void logTestConfiguration(TestConfig config) {
        log.info("Test Configuration to be used:");
        log.info("Base URL: {}", config.baseUrl());
        log.info("Browser: {}", config.browser());
        log.info("Environment: {}", config.environment());
        log.info("Execution: {}", config.execution());
        log.info("Language: {}", config.lang());
        log.info("Retry Count: {}", config.retryCount());
        log.info("Retry Delay (ms): {}", config.retryDelayMs());
    }
}

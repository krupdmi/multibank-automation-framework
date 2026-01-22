package org.multibank.ui.config;

public record TestConfig(
        String baseUrl,
        String browser,
        String environment,
        String execution,
        String lang,
        String platform,
        boolean headless,
        int retryCount,
        int retryDelayMs) {
}
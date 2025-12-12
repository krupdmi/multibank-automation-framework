package org.multibank.ui.config;

public record TestConfig(
        String baseUrl,
        String browser,
        String environment,
        String execution,
        String lang,
        int retryCount,
        int retryDelayMs) {
}

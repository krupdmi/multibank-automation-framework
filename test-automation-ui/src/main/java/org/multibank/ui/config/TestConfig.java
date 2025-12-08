package org.multibank.ui.config;

public record TestConfig(
        String baseUrl,
        String browser,
        String environment,
        String execution,
        int retryCount,
        int retryDelayMs) {
}

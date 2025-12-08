package org.multibank.ui.config;

public final class ParallelExecutionHandler {

    public static void configure(TestConfig config) {
        if ("parallel".equalsIgnoreCase(config.execution())) {
            System.setProperty("junit.jupiter.execution.parallel.enabled", "true");
            System.setProperty("junit.jupiter.execution.parallel.mode.default", "concurrent");
            System.setProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent");
        } else {
            System.setProperty("junit.jupiter.execution.parallel.enabled", "false");
        }
    }
}
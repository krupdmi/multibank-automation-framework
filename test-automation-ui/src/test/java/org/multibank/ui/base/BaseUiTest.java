package org.multibank.ui.base;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.multibank.core.enums.BrowserName;
import org.multibank.core.playwright.PlaywrightInstanceProvider;
import org.multibank.core.playwright.PlaywrightSession;
import org.multibank.ui.config.TestConfig;
import org.multibank.ui.config.TestConfigLoader;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Slf4j
public abstract class BaseUiTest {

    private static TestConfig config;
    private static final ThreadLocal<PlaywrightSession> sessionStorage = new ThreadLocal<>();

    @BeforeAll
    static void loadConfig() {
        config = TestConfigLoader.load();
    }

    @BeforeEach
    void setUp() {
        BrowserName browser = BrowserName.valueOf(config.browser().toUpperCase());
        PlaywrightSession session = PlaywrightInstanceProvider.createPage(browser, config.baseUrl());

        sessionStorage.set(session);
        log.info("Session started in thread: {}", Thread.currentThread().getName());
    }

    @AfterEach
    void tearDown() {
        PlaywrightSession session = sessionStorage.get();
        if (session != null) {
            session.close();
        }
        sessionStorage.remove();
        log.info("Session closed in thread: {}", Thread.currentThread().getName());
    }

    public Page page() {
        return sessionStorage.get().page();
    }
}

package org.multibank.ui.base;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.multibank.core.enums.BrowserName;
import org.multibank.core.playwright.PlaywrightInstanceProvider;
import org.multibank.core.playwright.PlaywrightSession;
import org.multibank.ui.config.TestConfig;
import org.multibank.ui.config.TestConfigLoader;
import org.multibank.ui.utils.ScreenshotUtils;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Slf4j
public abstract class BaseUiTest implements TestWatcher {

    private static TestConfig config;
    private static final ThreadLocal<PlaywrightSession> sessionStorage = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> testFailed = ThreadLocal.withInitial(() -> false);

    @BeforeAll
    static void loadConfig() {
        config = TestConfigLoader.load();
    }

    @BeforeEach
    void setUp() {
        testFailed.set(false);
        BrowserName browser = BrowserName.valueOf(config.browser().toUpperCase());
        PlaywrightSession session = PlaywrightInstanceProvider.createPage(browser, config.baseUrl());

        sessionStorage.set(session);
        log.info("Session started in thread: {}", Thread.currentThread().getName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        PlaywrightSession session = sessionStorage.get();
        if (session != null) {
            if (testFailed.get()) {
                captureScreenshotOnFailure(testInfo);
            }
            session.close();
        }
        sessionStorage.remove();
        testFailed.remove();
        log.info("Session closed in thread: {}", Thread.currentThread().getName());
    }

    public Page page() {
        return sessionStorage.get().page();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        testFailed.set(true);
        log.error("Test failed: {} - {}", context.getDisplayName(), cause.getMessage());
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("Test passed: {}", context.getDisplayName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.warn("Test aborted: {}", context.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("Test disabled: {} - {}", context.getDisplayName(), reason.orElse("No reason"));
    }

    private void captureScreenshotOnFailure(TestInfo testInfo) {
        try {
            String testName = testInfo.getDisplayName();
            ScreenshotUtils.captureAndAttach(page(), "Test Failure - " + testName);
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }
}

package org.multibank.ui.base;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.multibank.core.enums.BrowserName;
import org.multibank.core.playwright.PlaywrightInstanceProvider;
import org.multibank.core.playwright.PlaywrightSession;
import org.multibank.ui.config.TestConfig;
import org.multibank.ui.config.TestConfigLoader;
import org.multibank.ui.utils.ScreenshotUtils;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(BaseUiTest.ScreenshotExtension.class)
@Slf4j
public abstract class BaseUiTest {

    private static TestConfig config;
    private static final ThreadLocal<PlaywrightSession> sessionStorage = new ThreadLocal<>();
    private static final ThreadLocal<Throwable> testFailure = new ThreadLocal<>();

    @BeforeAll
    static void loadConfig() {
        config = TestConfigLoader.load();
    }

    @BeforeEach
    void setUp() {
        testFailure.remove();
        BrowserName browser = BrowserName.valueOf(config.browser().toUpperCase());
        PlaywrightSession session = PlaywrightInstanceProvider.createPage(browser, config.baseUrl());

        sessionStorage.set(session);
        log.info("Session started in thread: {}", Thread.currentThread().getName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        PlaywrightSession session = sessionStorage.get();
        if (session != null) {
            if (testFailure.get() != null) {
                captureScreenshotOnFailure(testInfo.getDisplayName());
            }
            session.close();
        }
        sessionStorage.remove();
        testFailure.remove();
        log.info("Session closed in thread: {}", Thread.currentThread().getName());
    }

    public Page page() {
        return sessionStorage.get().page();
    }

    private void captureScreenshotOnFailure(String testName) {
        try {
            ScreenshotUtils.captureAndAttach(page(), "Test Failure - " + testName);
            log.info("Screenshot captured for: {}", testName);
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    public static class ScreenshotExtension implements TestExecutionExceptionHandler {
        @Override
        public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
            testFailure.set(throwable);
            throw throwable;
        }
    }
}
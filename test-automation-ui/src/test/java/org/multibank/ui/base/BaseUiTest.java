package org.multibank.ui.base;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.multibank.core.enums.BrowserName;
import org.multibank.core.playwright.PlaywrightInstanceProvider;
import org.multibank.core.playwright.PlaywrightSession;
import org.multibank.ui.config.TestConfig;
import org.multibank.ui.config.TestConfigLoader;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public abstract class BaseUiTest {

    protected Page page;
    private PlaywrightSession session;
    protected TestConfig uiConfig;

    @BeforeAll
    void globalSetUp() {
        uiConfig = TestConfigLoader.load();
        String baseUrl = uiConfig.baseUrl();
        BrowserName browserName = BrowserName.valueOf(uiConfig.browser().toUpperCase());

        log.info("Starting browser: {} at {}", browserName, baseUrl);

        session = PlaywrightInstanceProvider.createPage(browserName, baseUrl);
        page = session.page();
    }

    @AfterAll
    void globalTearDown() {
        if (session != null) {
            log.info("Closing Playwright Session");
            session.close();
        }
    }

    public Page page() {
        return page;
    }
}

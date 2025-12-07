package org.multibank.ui.base;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.multibank.core.enums.BrowserName;
import org.multibank.core.ui.PlaywrightInstanceProvider;
import org.multibank.ui.config.TestConfig;
import org.multibank.ui.config.TestConfigLoader;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseUiTest {

    protected Page page;
    protected TestConfig uiConfig;

    @BeforeAll
    void globalSetUp() {
        uiConfig = TestConfigLoader.load();
        BrowserName browserName = BrowserName.valueOf(uiConfig.browser().toUpperCase());
        page = PlaywrightInstanceProvider.createPage(browserName, uiConfig.baseUrl());
    }

    @AfterAll
    void globalTearDown() {
        if (page != null) {
            try {
                page.context().browser().close();
                page.context().close();
            } catch (Exception ignored) {
            }
        }
    }

    public Page page() {
        return page;
    }
}

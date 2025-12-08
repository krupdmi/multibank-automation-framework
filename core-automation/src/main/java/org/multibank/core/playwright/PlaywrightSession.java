package org.multibank.core.playwright;

import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record PlaywrightSession(
        Playwright playwright,
        Browser browser,
        BrowserContext context,
        Page page
) implements AutoCloseable {

    @Override
    public void close() {
        log.info("Closing Playwright session");

        safeClose("BrowserContext", context::close);
        safeClose("Browser", browser::close);
        safeClose("Playwright", playwright::close);

        log.info("Playwright session closed");
    }

    private void safeClose(String component, Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            log.warn("Failed to close {}: {}", component, e.getMessage());
        }
    }
}

package org.multibank.ui.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Slf4j
public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    /**
     * Captures a screenshot and add it to Allure report
     *
     * @param page current page
     * @param name name for the screenshot
     */
    public static void captureAndAttach(Page page, String name) {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
            log.info("Screenshot captured: {}", name);
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }
}

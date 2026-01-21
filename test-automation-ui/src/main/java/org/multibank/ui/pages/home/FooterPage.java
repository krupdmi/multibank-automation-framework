package org.multibank.ui.pages.home;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.pages.BasePage;

@Slf4j
public class FooterPage extends BasePage {

    private static final String BANNER_SLIDER = ".slick-slider.style_slider__EZV_4";
    private static final String BANNER_SLIDE = ".slick-slide:not(.slick-cloned)";
    private static final String DOWNLOAD_CONTAINER = ".style_buttons-container__9n_JQ";
    private static final String APP_STORE_LINK = "a[href*='apps.apple.com']";
    private static final String GOOGLE_PLAY_LINK = "a[href*='play.google.com']";
    private static final String HREF = "href";

    public FooterPage(Page page) {
        super(page);
    }

    @Step("Check if banner slider is visible")
    public boolean isBannerSliderVisible() {
        return actions.isLocatorVisible(locator(BANNER_SLIDER).first());
    }

    @Step("Get banner count")
    public int getBannerCount() {
        return locator(BANNER_SLIDE).count();
    }

    @Step("Check if banner at index {index} is visible")
    public boolean isBannerVisible(int index) {
        return actions.isLocatorVisible(locator(BANNER_SLIDE).nth(index));
    }

    @Step("Scroll to the footer section")
    public void scrollToFooter() {
        actions.scrollIntoView(locator(DOWNLOAD_CONTAINER).first());
    }

    @Step("Get App Store link href")
    public String getAppStoreLinkHref() {
        return actions.getAttribute(locator(APP_STORE_LINK).first(), HREF);
    }

    @Step("Get Google Play link href")
    public String getGooglePlayLinkHref() {
        return actions.getAttribute(locator(GOOGLE_PLAY_LINK).first(), HREF);
    }
}
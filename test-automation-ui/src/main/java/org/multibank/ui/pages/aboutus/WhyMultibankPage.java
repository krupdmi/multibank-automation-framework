package org.multibank.ui.pages.aboutus;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.pages.BasePage;

@Slf4j
public class WhyMultibankPage extends BasePage {

    private static final String MAIN_HEADING = "h1.heading-1";
    private static final String FEATURE_CARD = ".cardSection_card-div__vdowX";
    private static final String STATS_CARD = ".established_card-div__fF1Jf";

    public WhyMultibankPage(Page page) {
        super(page);
    }

    @Step("Get main heading text")
    public String getMainHeading() {
        page.waitForLoadState();
        return page.locator(MAIN_HEADING).first().textContent().trim();
    }

    @Step("Count feature cards")
    public int getFeatureCardCount() {
        return page.locator(FEATURE_CARD).count();
    }

    @Step("Count stats cards")
    public int getStatsCardCount() {
        return page.locator(STATS_CARD).count();
    }
}
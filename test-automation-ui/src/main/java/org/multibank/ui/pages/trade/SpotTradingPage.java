package org.multibank.ui.pages.trade;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.multibank.ui.constants.TestConstants;
import org.multibank.ui.pages.BasePage;

@Slf4j
public class SpotTradingPage extends BasePage {

    // container locators
    private static final String TRADE_PAIRS_TABLE = "#trade-pairs";
    private static final String TRADE_PAIRS_SCROLLER = ".h-100 >.style_container__pShEu >.style_scroller__yxXrE";

    // dynamic locators templates
    private static final String PAIR_NAME_TEMPLATE = "[id='pair-name-%s']";
    private static final String PAIR_FAVORITE_TEMPLATE = "[id='pair-name-%s-favorite']";
    private static final String PAIR_PRICE_TEMPLATE = "[id='trade-pairs-%s_price-td']";
    private static final String PAIR_24H_CHANGE_TEMPLATE = "[id='pair-%s-24h-change']";

    public SpotTradingPage(Page page) {
        super(page);
    }

    @Step("Click on trade pair selector to open pairs panel")
    public void openTradePairsPanel(String selector) {
        Locator tradePairSelector = locator(selector);
        actions.click(tradePairSelector);
        locator(TRADE_PAIRS_TABLE).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        log.info("Trade pairs panel opened");
    }

    @Step("Scroll to trading pair '{pairId}' in the list")
    public boolean scrollToPair(String pairId) {
        Locator pairLocator = pairNameLocator(pairId);
        Locator scroll = locator(TRADE_PAIRS_SCROLLER).first();
        actions.scrollContainerToTop(scroll);

        if (actions.isLocatorVisible(pairLocator, TestConstants.POLL_INTERVAL_MS)) {
            return true;
        }

        for (int i = 0; i < TestConstants.MAX_SCROLL_ATTEMPTS; i++) {
            actions.scrollContainerBy(scroll, TestConstants.SCROLL_STEP_PX);

            if (actions.isLocatorVisible(pairLocator, TestConstants.POLL_INTERVAL_MS)) {
                log.info("Found pair {} after {} scroll(s)", pairId, i + 1);
                return true;
            }
        }

        log.warn("Could not find pair {} after {} scroll attempts", pairId, TestConstants.MAX_SCROLL_ATTEMPTS);
        return false;
    }

    @Step("Check if trading pair '{pairId}' has favorite element")
    public boolean hasFavoriteElement(String pairId) {
        return actions.isLocatorVisible(pairFavoriteLocator(pairId));
    }

    @Step("Check if trading pair '{pairId}' has name element with correct id")
    public boolean hasNameElement(String pairId) {
        return actions.isLocatorVisible(pairNameLocator(pairId));
    }

    @Step("Get trading pair '{pairId}' display name")
    public String getPairDisplayName(String pairId) {
        return actions.getText(pairNameLocator(pairId));
    }

    @Step("Check if trading pair '{pairId}' has price element")
    public boolean hasPriceElement(String pairId) {
        return actions.isLocatorVisible(pairPriceLocator(pairId));
    }

    @Step("Get trading pair '{pairId}' price")
    public String getPairPrice(String pairId) {
        return actions.getText(pairPriceLocator(pairId));
    }

    @Step("Check if trading pair '{pairId}' has 24h change element")
    public boolean has24hChangeElement(String pairId) {
        return actions.isLocatorVisible(pair24hChangeLocator(pairId));
    }

    @Step("Get trading pair '{pairId}' 24h change value")
    public String getPair24hChange(String pairId) {
        return actions.getText(pair24hChangeLocator(pairId));
    }

    private Locator pairNameLocator(String pairId) {
        return locator(String.format(PAIR_NAME_TEMPLATE, pairId));
    }

    private Locator pairFavoriteLocator(String pairId) {
        return locator(String.format(PAIR_FAVORITE_TEMPLATE, pairId));
    }

    private Locator pairPriceLocator(String pairId) {
        return locator(String.format(PAIR_PRICE_TEMPLATE, pairId));
    }

    private Locator pair24hChangeLocator(String pairId) {
        return locator(String.format(PAIR_24H_CHANGE_TEMPLATE, pairId));
    }
}
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

    private static final String TRADE_PAIRS_TABLE = "#trade-pairs";
    private static final String TRADE_PAIRS_SCROLLER = ".h-100 >.style_container__pShEu >.style_scroller__yxXrE";

    public SpotTradingPage(Page page) {
        super(page);
    }

    @Step("Click on trade pair selector to open pairs panel")
    public void openTradePairsPanel(String selector) {
        Locator tradePairSelector = locator(selector);
        actions.click(tradePairSelector);
        locator(TRADE_PAIRS_TABLE).waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );
        log.info("Trade pairs panel opened");
    }

    @Step("Scroll to trading pair '{pairId}' in the list")
    public boolean scrollToPair(String pairId) {
        Locator pairNameLocator = locator(String.format("[id='pair-name-%s']", pairId));
        Locator scroller = locator(TRADE_PAIRS_SCROLLER).first();
        actions.scrollContainerToTop(scroller);

        if (actions.waitForVisibleSafe(pairNameLocator, TestConstants.POLL_INTERVAL_MS)) {
            return true;
        }

        for (int i = 0; i < TestConstants.MAX_SCROLL_ATTEMPTS; i++) {
            actions.scrollContainerBy(scroller, TestConstants.SCROLL_STEP_PX);

            if (actions.waitForVisibleSafe(pairNameLocator, TestConstants.POLL_INTERVAL_MS)) {
                log.info("Found pair {} after {} scroll(s)", pairId, i + 1);
                return true;
            }
        }

        log.warn("Could not find pair {} after {} scroll attempts", pairId, TestConstants.MAX_SCROLL_ATTEMPTS);
        return false;
    }

    @Step("Check if trading pair '{pairId}' has favorite element")
    public boolean hasFavoriteElement(String pairId) {
        Locator favoriteLocator = locator(String.format("[id='pair-name-%s-favorite']", pairId));
        return actions.isLocatorVisible(favoriteLocator);
    }

    @Step("Check if trading pair '{pairId}' has name element with correct id")
    public boolean hasNameElement(String pairId) {
        Locator nameLocator = locator(String.format("[id='pair-name-%s']", pairId));
        return actions.isLocatorVisible(nameLocator);
    }

    @Step("Get trading pair '{pairId}' display name")
    public String getPairDisplayName(String pairId) {
        Locator nameLocator = locator(String.format("[id='pair-name-%s']", pairId));
        return actions.getText(nameLocator);
    }

    @Step("Check if trading pair '{pairId}' has price element")
    public boolean hasPriceElement(String pairId) {
        Locator priceLocator = locator(String.format("[id='trade-pairs-%s_price-td']", pairId));
        return actions.isLocatorVisible(priceLocator);
    }

    @Step("Get trading pair '{pairId}' price")
    public String getPairPrice(String pairId) {
        Locator priceLocator = locator(String.format("[id='trade-pairs-%s_price-td']", pairId));
        return actions.getText(priceLocator);
    }

    @Step("Check if trading pair '{pairId}' has 24h change element")
    public boolean has24hChangeElement(String pairId) {
        Locator changeLocator = locator(String.format("[id='pair-%s-24h-change']", pairId));
        return actions.isLocatorVisible(changeLocator);
    }

    @Step("Get trading pair '{pairId}' 24h change value")
    public String getPair24hChange(String pairId) {
        Locator changeLocator = locator(String.format("[id='pair-%s-24h-change']", pairId));
        return actions.getText(changeLocator);
    }
}
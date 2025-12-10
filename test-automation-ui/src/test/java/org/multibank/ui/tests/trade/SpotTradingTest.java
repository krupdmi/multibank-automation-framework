package org.multibank.ui.tests.trade;

import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.multibank.ui.assertions.Assertions;
import org.multibank.ui.base.BaseUiTest;
import org.multibank.ui.dataprovider.SpotTradingDataProvider;
import org.multibank.ui.models.SpotTradingData;
import org.multibank.ui.pages.home.HeaderPage;
import org.multibank.ui.pages.trade.SpotTradingPage;

@Epic("Trading Functionality")
@Feature("Spot Trading")
@Tag("Regression")
@Slf4j
class SpotTradingTest extends BaseUiTest {

    @Test
    @DisplayName("Spot trading section displays trading pairs across different categories")
    @Story("User views spot trading pairs with correct data structure")
    void spotTradingSectionShouldDisplayTradingPairsWithCorrectStructure() {
        SpotTradingData data = SpotTradingDataProvider.loadSpotTradingData();
        SpotTradingData.Navigation navigation = data.getNavigation();

        HeaderPage header = new HeaderPage(page());
        header.openDropdown(navigation.getDropdown());
        header.clickDropdownItem(navigation.getItem());

        page().waitForLoadState();
        log.info("Navigated to Spot trading page");

        SpotTradingPage spotTradingPage = new SpotTradingPage(page());
        spotTradingPage.openTradePairsPanel(data.getTradePairSelector());

        log.info("Validating {} trading pairs", data.getTradingPairs().size());

        for (SpotTradingData.TradingPair pair : data.getTradingPairs()) {
            String pairId = pair.getPairId();
            String expectedDisplayName = pair.getDisplayName();

            log.info("Validating trading pair: {} ({})", pairId, expectedDisplayName);

            boolean found = spotTradingPage.scrollToPair(pairId);
            Assertions.expectTrue(
                    String.format("Trading pair '%s' should be found in the list", pairId),
                    found
            );

            if (!found) {
                log.warn("Skipping validation for pair {} - not found", pairId);
                continue;
            }

            Assertions.expectTrue(
                    String.format("Trading pair '%s' should have favorite element", pairId),
                    spotTradingPage.hasFavoriteElement(pairId)
            );

            Assertions.expectTrue(
                    String.format("Trading pair '%s' should have name element with correct id", pairId),
                    spotTradingPage.hasNameElement(pairId)
            );

            String actualDisplayName = spotTradingPage.getPairDisplayName(pairId);
            log.info("Pair {} display name: {}", pairId, actualDisplayName);

            Assertions.expectTrue(
                    String.format("Trading pair '%s' display name should match expected '%s', actual '%s'",
                                  pairId, expectedDisplayName, actualDisplayName),
                    expectedDisplayName.equals(actualDisplayName)
            );

            Assertions.expectTrue(
                    String.format("Trading pair '%s' should have price element", pairId),
                    spotTradingPage.hasPriceElement(pairId)
            );

            String price = spotTradingPage.getPairPrice(pairId);
            log.info("Pair {} price: {}", pairId, price);

            Assertions.expectTrue(
                    String.format("Trading pair '%s' price should not be empty", pairId),
                    price != null && !price.isEmpty()
            );

            Assertions.expectTrue(
                    String.format("Trading pair '%s' should have 24h change element", pairId),
                    spotTradingPage.has24hChangeElement(pairId)
            );

            String change24h = spotTradingPage.getPair24hChange(pairId);
            log.info("Pair {} 24h change: {}", pairId, change24h);

            Assertions.expectTrue(
                    String.format("Trading pair '%s' 24h change should not be empty", pairId),
                    change24h != null && !change24h.isEmpty()
            );
        }

        Assertions.assertAll();
        log.info("All trading pairs validated successfully");
    }
}
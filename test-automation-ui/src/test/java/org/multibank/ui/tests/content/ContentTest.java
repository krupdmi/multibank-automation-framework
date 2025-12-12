package org.multibank.ui.tests.content;

import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.multibank.ui.assertions.Assertions;
import org.multibank.ui.base.BaseUiTest;
import org.multibank.ui.dataprovider.ContentValidationDataProvider;
import org.multibank.ui.dataprovider.NavigationDataProvider;
import org.multibank.ui.models.ContentValidationData;
import org.multibank.ui.models.LanguageNavigation;
import org.multibank.ui.pages.aboutus.WhyMultibankPage;
import org.multibank.ui.pages.home.FooterPage;
import org.multibank.ui.pages.home.HeaderPage;

@Epic("Content Validation")
@Feature("Footer and Marketing Content")
@Tag("Regression")
@Slf4j
class ContentTest extends BaseUiTest {

    private HeaderPage header;
    private ContentValidationData contentData;

    @BeforeEach
    void setUpContent() {
        header = new HeaderPage(page());
        LanguageNavigation languageNavigation = NavigationDataProvider.loadForConfiguredLanguage();
        contentData = ContentValidationDataProvider.loadForConfiguredLanguage();

        log.info("Selecting language: {}", languageNavigation.getLanguageLabel());
        header.selectLanguage(languageNavigation.getLanguageLabel());
    }

    @Test
    @DisplayName("Check that marketing banners appear at the bottom of the home page")
    @Story("User can see marketing banners")
    void marketingBannersShouldAppearAtBottomOfPage() {
        FooterPage footer = new FooterPage(page());

        ContentValidationData.MarketingBanners expected = contentData.getMarketingBanners();

        footer.scrollToFooter();

        Assertions.expectTrue(
                "Banner slider should be visible",
                footer.isBannerSliderVisible()
        );

        int actualCount = footer.getBannerCount();
        log.info("Found {} banners", actualCount);

        Assertions.expectTrue(
                String.format("Should have at least %d banners, found %d",
                              expected.getMinBannerCount(), actualCount),
                actualCount >= expected.getMinBannerCount()
        );

        if (actualCount > 0) {
            Assertions.expectTrue(
                    "First banner should be visible",
                    footer.isBannerVisible(0)
            );
        }

        Assertions.assertAll();
    }

    @Test
    @DisplayName("Validate download section links to App Store and Google Play")
    @Story("User can download mobile apps")
    void downloadSectionShouldLinkToAppStores() {
        FooterPage footer = new FooterPage(page());

        ContentValidationData.DownloadSection expected = contentData.getDownloadSection();

        footer.scrollToFooter();

        String appStoreLink = footer.getAppStoreLinkHref();
        log.info("App Store link: {}", appStoreLink);

        Assertions.expectTrue(
                "App Store link should match expected pattern",
                matchesPattern(appStoreLink, expected.getAppStorePattern())
        );

        String googlePlayLink = footer.getGooglePlayLinkHref();
        log.info("Google Play link: {}", googlePlayLink);

        Assertions.expectTrue(
                "Google Play link should match expected pattern",
                matchesPattern(googlePlayLink, expected.getGooglePlayPattern())
        );

        Assertions.assertAll();
    }

    @Test
    @DisplayName("Check that 'Why Multibank?' page renders expected components")
    @Story("User can view 'Why Multibank?' page")
    void whyMultiBankPageShouldRenderExpectedComponents() {
        ContentValidationData.Navigation navigation = contentData.getNavigation();
        ContentValidationData.WhyMultiBankPage expected = contentData.getWhyMultiBankPage();

        header.openDropdown(navigation.getDropdown());
        header.clickDropdownItem(navigation.getItem());
        page().waitForLoadState();

        WhyMultibankPage whyMultibankPage = new WhyMultibankPage(page());

        String actualHeading = whyMultibankPage.getMainHeading();
        log.info("Page heading: {}", actualHeading);

        Assertions.expectTrue(
                "Page should display correct heading",
                actualHeading.contains(expected.getExpectedHeading())
        );

        int featureCount = whyMultibankPage.getFeatureCardCount();
        log.info("Found {} feature cards", featureCount);

        Assertions.expectTrue(
                String.format("Should have at least %d features, actual found %d",
                              expected.getMinFeatureCount(), featureCount),
                featureCount >= expected.getMinFeatureCount()
        );

        int statsCount = whyMultibankPage.getStatsCardCount();
        log.info("Found {} stats cards", statsCount);

        Assertions.expectTrue(
                String.format("Should have at least %d stats, actual found %d",
                              expected.getMinStatsCount(), statsCount),
                statsCount >= expected.getMinStatsCount()
        );

        Assertions.assertAll();
    }

    private boolean matchesPattern(String value, String pattern) {
        if (value == null || pattern == null) {
            return false;
        }
        for (String p : pattern.split("\\|")) {
            if (value.toLowerCase().contains(p.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
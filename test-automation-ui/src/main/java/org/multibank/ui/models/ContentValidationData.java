package org.multibank.ui.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContentValidationData {

    @JsonProperty("navigation")
    private Navigation navigation;

    @JsonProperty("downloadSection")
    private DownloadSection downloadSection;

    @JsonProperty("marketingBanners")
    private MarketingBanners marketingBanners;

    @JsonProperty("whyMultiBankPage")
    private WhyMultiBankPage whyMultiBankPage;

    @Data
    public static class Navigation {
        @JsonProperty("dropdown")
        private String dropdown;

        @JsonProperty("item")
        private String item;
    }

    @Data
    public static class DownloadSection {
        @JsonProperty("appStorePattern")
        private String appStorePattern;

        @JsonProperty("googlePlayPattern")
        private String googlePlayPattern;
    }

    @Data
    public static class MarketingBanners {
        @JsonProperty("minBannerCount")
        private int minBannerCount;
    }

    @Data
    public static class WhyMultiBankPage {
        @JsonProperty("expectedHeading")
        private String expectedHeading;

        @JsonProperty("minFeatureCount")
        private int minFeatureCount;

        @JsonProperty("minStatsCount")
        private int minStatsCount;
    }
}

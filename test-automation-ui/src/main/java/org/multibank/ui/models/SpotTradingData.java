package org.multibank.ui.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SpotTradingData {

    @JsonProperty("navigation")
    private Navigation navigation;

    @JsonProperty("tradePairSelector")
    private String tradePairSelector;

    @JsonProperty("tradingPairs")
    private List<TradingPair> tradingPairs;

    @Data
    public static class Navigation {
        @JsonProperty("dropdown")
        private String dropdown;

        @JsonProperty("item")
        private String item;
    }

    @Data
    public static class TradingPair {
        @JsonProperty("pairId")
        private String pairId;

        @JsonProperty("displayName")
        private String displayName;
    }
}
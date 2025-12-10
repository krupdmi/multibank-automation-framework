package org.multibank.ui.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NavigationMenuData {

    @JsonProperty("topNavigationItems")
    private List<String> topNavigationItems;

    @JsonProperty("userMenuItems")
    private List<String> userMenuItems;

    @JsonProperty("dropdowns")
    private Map<String, List<String>> dropdowns;
}

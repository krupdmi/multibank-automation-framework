package org.multibank.ui.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NavigationItem {

    @JsonProperty("label")
    private String label;

    @JsonProperty("link")
    private String link;

    @JsonProperty("subItem")
    private List<NavigationItem> subItem;

    public boolean hasSubItem() {
        return subItem != null && !subItem.isEmpty();
    }
}

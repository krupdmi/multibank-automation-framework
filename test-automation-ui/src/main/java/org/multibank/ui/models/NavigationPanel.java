package org.multibank.ui.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NavigationPanel {

    @JsonProperty("items")
    private List<NavigationItem> items;
}
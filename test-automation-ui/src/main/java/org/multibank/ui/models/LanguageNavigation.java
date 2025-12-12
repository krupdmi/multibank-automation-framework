package org.multibank.ui.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LanguageNavigation {

    @JsonProperty("basePath")
    private String basePath;

    @JsonProperty("languageLabel")
    private String languageLabel;

    @JsonProperty("topNavigationPanel")
    private NavigationPanel topNavigationPanel;

    @JsonProperty("userAction")
    private NavigationPanel userAction;
}

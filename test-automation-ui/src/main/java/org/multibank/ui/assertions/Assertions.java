package org.multibank.ui.assertions;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.AssertJMultipleFailuresError;

public final class Assertions {

    private static final ThreadLocal<SoftAssertions> SOFTLY =
            ThreadLocal.withInitial(SoftAssertions::new);

    public static void expectVisible(String elementName, boolean isVisible) {
        SOFTLY.get()
                .assertThat(isVisible)
                .as("Expecting '%s' to be visible", elementName)
                .isTrue();
    }

    public static void expectTrue(String description, boolean condition) {
        SOFTLY.get()
                .assertThat(condition)
                .as(description)
                .isTrue();
    }

    public static void reset() {
        SOFTLY.remove();
    }

    public static void assertAll() {
        SoftAssertions softly = SOFTLY.get();
        try {
            softly.assertAll();
        } catch (AssertJMultipleFailuresError e) {
            throw e;
        } finally {
            reset();
        }
    }
}

package turing.config;

import java.util.Arrays;
import java.util.Locale;

/**
 * Types of user interface the application can start with.
 */
public enum UiType {
    CONSOLE,
    SWING;

    /**
     * Parses the value written in the configuration, ignoring case and surrounding spaces.
     * @param value the configured value, e.g. "console" or "swing"
     * @return the matching interface type
     * @throws IllegalArgumentException if the value does not match any type
     */
    public static UiType from(String value) {
        if (value != null) {
            String normalized = value.trim().toUpperCase(Locale.ROOT);
            for (UiType type : values()) {
                if (type.name().equals(normalized)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("ui.type invalido: '" + value + "'. Valores posibles: "
                + Arrays.toString(values()).toLowerCase(Locale.ROOT));
    }
}

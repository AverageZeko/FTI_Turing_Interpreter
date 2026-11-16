package turing.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class AppConfigTest {

    @Test
    void missingKeysUseDefaults() {
        AppConfig config = AppConfig.from(new Properties());

        assertEquals(UiType.CONSOLE, config.getUiType());
        assertEquals("rule_110_trace", config.getMachineRule());
        assertEquals(200000, config.getMaxSteps());
        assertEquals("light", config.getGuiTheme());
    }

    @Test
    void readsApplicationPropertiesFromClasspath() throws IOException {
        Properties file = new Properties();
        try (InputStream input = AppConfig.class.getResourceAsStream("/application.properties")) {
            file.load(input);
        }

        AppConfig config = AppConfig.load(new String[0]);

        assertEquals(UiType.from(file.getProperty(AppConfig.UI_TYPE)), config.getUiType());
        assertEquals(file.getProperty(AppConfig.MACHINE_RULE), config.getMachineRule());
    }

    @Test
    void commandLineArgumentOverridesFile() {
        AppConfig config = AppConfig.load(new String[] { "--ui.type=swing", "--gui.max-steps=50" });

        assertEquals(UiType.SWING, config.getUiType());
        assertEquals(50, config.getMaxSteps());
    }

    @Test
    void systemPropertyOverridesFile() {
        System.setProperty(AppConfig.UI_TYPE, "swing");
        try {
            assertEquals(UiType.SWING, AppConfig.load(new String[0]).getUiType());
        } finally {
            System.clearProperty(AppConfig.UI_TYPE);
        }
    }

    @Test
    void commandLineArgumentOverridesSystemProperty() {
        System.setProperty(AppConfig.UI_TYPE, "swing");
        try {
            assertEquals(UiType.CONSOLE, AppConfig.load(new String[] { "--ui.type=console" }).getUiType());
        } finally {
            System.clearProperty(AppConfig.UI_TYPE);
        }
    }

    @Test
    void argumentsWithoutTheExpectedFormatAreIgnored() {
        Properties parsed = AppConfig.parseArgs(new String[] { "swing", "-ui.type=swing", "--=x", "--ui.type" });

        assertTrue(parsed.isEmpty());
    }

    @Test
    void uiTypeIgnoresCaseAndSpaces() {
        assertEquals(UiType.SWING, UiType.from("  Swing "));
    }

    @Test
    void invalidUiTypeListsValidValues() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> UiType.from("web"));

        assertTrue(error.getMessage().contains("console"));
        assertTrue(error.getMessage().contains("swing"));
    }

    @Test
    void maxStepsMustBePositive() {
        Properties properties = new Properties();
        properties.setProperty(AppConfig.GUI_MAX_STEPS, "0");

        assertThrows(IllegalArgumentException.class, () -> AppConfig.from(properties));
    }
}

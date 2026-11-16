package turing.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Application configuration read from application.properties.
 * <p>
 * Each value is resolved with this priority (highest first):
 * <ol>
 * <li>command line argument: {@code --ui.type=swing}</li>
 * <li>system property: {@code -Dui.type=swing}</li>
 * <li>the application.properties file in the classpath</li>
 * <li>the default value defined in this class</li>
 * </ol>
 */
public final class AppConfig {

    public static final String UI_TYPE = "ui.type";
    public static final String MACHINE_RULE = "machine.rule";
    public static final String GUI_MAX_STEPS = "gui.max-steps";
    public static final String GUI_THEME = "gui.theme";

    private static final String CONFIG_FILE = "/application.properties";
    private static final String ARG_PREFIX = "--";

    private final UiType uiType;
    private final String machineRule;
    private final int maxSteps;
    private final String guiTheme;

    private AppConfig(Properties properties) {
        this.uiType = UiType.from(properties.getProperty(UI_TYPE, "console"));
        this.machineRule = properties.getProperty(MACHINE_RULE, "rule_110_trace").trim();
        this.maxSteps = parsePositiveInt(GUI_MAX_STEPS, properties.getProperty(GUI_MAX_STEPS, "200000"));
        this.guiTheme = properties.getProperty(GUI_THEME, "light").trim();
    }

    /**
     * Loads the configuration from the file, the system properties and the command line arguments.
     * @param args the arguments received by main, in the form --key=value
     * @return the resolved configuration
     */
    public static AppConfig load(String[] args) {
        Properties properties = readConfigFile();
        for (String key : new String[] { UI_TYPE, MACHINE_RULE, GUI_MAX_STEPS, GUI_THEME }) {
            String systemValue = System.getProperty(key);
            if (systemValue != null) {
                properties.setProperty(key, systemValue);
            }
        }
        properties.putAll(parseArgs(args));
        return new AppConfig(properties);
    }

    /**
     * Builds a configuration only from the given properties (missing keys take their default value).
     */
    public static AppConfig from(Properties properties) {
        return new AppConfig(properties);
    }

    /**
     * Converts arguments like --ui.type=swing into properties. Arguments with another format are ignored.
     */
    static Properties parseArgs(String[] args) {
        Properties result = new Properties();
        if (args == null) {
            return result;
        }
        for (String arg : args) {
            int separator = arg.indexOf('=');
            if (arg.startsWith(ARG_PREFIX) && separator > ARG_PREFIX.length()) {
                result.setProperty(arg.substring(ARG_PREFIX.length(), separator), arg.substring(separator + 1));
            }
        }
        return result;
    }

    private static Properties readConfigFile() {
        Properties properties = new Properties();
        try (InputStream input = AppConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                    properties.load(reader);
                }
            }
        } catch (IOException anException) {
            throw new UncheckedIOException("No se pudo leer " + CONFIG_FILE, anException);
        }
        return properties;
    }

    private static int parsePositiveInt(String key, String value) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed > 0) {
                return parsed;
            }
        } catch (NumberFormatException ignored) {
            // falls through to the error below
        }
        throw new IllegalArgumentException(key + " debe ser un entero positivo, se recibio: '" + value + "'");
    }

    public UiType getUiType() {
        return uiType;
    }

    public String getMachineRule() {
        return machineRule;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public String getGuiTheme() {
        return guiTheme;
    }
}

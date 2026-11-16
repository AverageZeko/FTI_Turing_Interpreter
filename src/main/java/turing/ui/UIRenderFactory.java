package turing.ui;

import java.awt.GraphicsEnvironment;

import turing.config.AppConfig;
import turing.ui.console.ConsoleApp;
import turing.ui.gui.SwingApp;

/**
 * Creates the user interface specified in the configuration.
 * This is the only class that knows the concrete implementations of {@link UIRender}.
 */
public final class UIRenderFactory {

    private UIRenderFactory() {
    }

    /**
     * @param config the application configuration
     * @return the interface for the configured ui.type
     */
    public static UIRender create(AppConfig config) {
        // No default branch on purpose: adding a new UiType without handling it here is a compile error
        return switch (config.getUiType()) {
            case CONSOLE -> new ConsoleApp(config);
            case SWING -> createSwing(config);
        };
    }

    private static UIRender createSwing(AppConfig config) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("No hay entorno grafico disponible; se usa la interfaz de consola.");
            return new ConsoleApp(config);
        }
        return new SwingApp(config);
    }
}

package turing.ui.gui;

import java.io.UncheckedIOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import turing.config.AppConfig;
import turing.model.turing_machine.TuringMachine;
import turing.services.SimulationService;
import turing.ui.UIRender;

/**
 * Graphical interface built with Swing.
 */
public class SwingApp implements UIRender {
    private final AppConfig config;
    private final SimulationService simulationService;

    public SwingApp(AppConfig config) {
        this.config = config;
        this.simulationService = new SimulationService();
    }

    /**
     * Opens the main window. Returns immediately: the application keeps running on the
     * Swing event thread until the window is closed.
     */
    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            applyTheme();
            TuringMachine machine;
            try {
                machine = simulationService.loadMachine(config.getMachineRule());
            } catch (IllegalArgumentException | UncheckedIOException anException) {
                JOptionPane.showMessageDialog(null, anException.getMessage(), "No se pudo cargar la maquina",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            new MainFrame(config, simulationService, machine).setVisible(true);
        });
    }

    private void applyTheme() {
        boolean themeApplied = "dark".equalsIgnoreCase(config.getGuiTheme())
                ? FlatDarkLaf.setup()
                : FlatLightLaf.setup();
        if (!themeApplied) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ignored) {
                // keeps the default look and feel
            }
        }
    }
}

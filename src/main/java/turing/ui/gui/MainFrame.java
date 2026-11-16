package turing.ui.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

import turing.config.AppConfig;
import turing.model.execution.ExecutionTrace;
import turing.model.turing_machine.TuringMachine;
import turing.services.Rule110TapeReader;
import turing.services.SimulationService;

/**
 * Main window: input form on top, tape in the middle, trace / generations / machine tabs and
 * playback controls at the bottom.
 */
class MainFrame extends JFrame {
    private final AppConfig config;
    private final SimulationService simulationService;
    private final TuringMachine machine;
    private final PlaybackModel playback = new PlaybackModel();

    private final InputPanel inputPanel;
    private final TapePanel tapePanel = new TapePanel();
    private final JLabel statusLabel = new JLabel("Escriba una palabra y presione Ejecutar.");
    private final TraceTableModel traceTableModel = new TraceTableModel();
    private final JTable traceTable = new JTable(traceTableModel);
    private final GenerationsPanel generationsPanel = new GenerationsPanel();
    private final PlaybackControls controls;
    private boolean syncingSelection;

    MainFrame(AppConfig config, SimulationService simulationService, TuringMachine machine) {
        super("Maquina de Turing - " + machine.getName());
        this.config = config;
        this.simulationService = simulationService;
        this.machine = machine;
        this.inputPanel = new InputPanel(this::simulate);
        this.controls = new PlaybackControls(playback);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        configureTable();
        playback.addListener(this::showCurrentConfiguration);

        setMinimumSize(new Dimension(900, 600));
        setSize(1200, 760);
        setLocationRelativeTo(null);
    }

    private JPanel buildCenter() {
        JScrollPane tapeScroll = new JScrollPane(tapePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tapeScroll.setBorder(BorderFactory.createEmptyBorder());
        tapeScroll.getHorizontalScrollBar().setUnitIncrement(30);

        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 6, 12));

        JPanel tapeSection = new JPanel(new BorderLayout());
        tapeSection.setBorder(BorderFactory.createTitledBorder("Cinta"));
        tapeSection.add(tapeScroll, BorderLayout.CENTER);
        tapeSection.add(statusLabel, BorderLayout.SOUTH);

        JScrollPane generationsScroll = new JScrollPane(generationsPanel);
        generationsScroll.getVerticalScrollBar().setUnitIncrement(18);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Traza", new JScrollPane(traceTable));
        tabs.addTab("Generaciones", generationsScroll);
        tabs.addTab("Maquina", new JScrollPane(new MachineInfoPanel(machine)));

        JPanel center = new JPanel(new BorderLayout(0, 6));
        center.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        center.add(tapeSection, BorderLayout.NORTH);
        center.add(tabs, BorderLayout.CENTER);
        return center;
    }

    private void configureTable() {
        traceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        traceTable.setFillsViewportHeight(true);
        traceTable.getTableHeader().setReorderingAllowed(false);
        traceTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || syncingSelection) {
                return;
            }
            int row = traceTable.getSelectedRow();
            if (row >= 0) {
                controls.stop();
                playback.setIndex(row);
            }
        });
        int[] widths = { 60, 150, 50, 60, 90, 150, 70 };
        for (int i = 0; i < widths.length; i++) {
            traceTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Runs the machine in a background thread, so the window does not freeze on long executions.
     */
    private void simulate(String word, String iterations) {
        controls.stop();
        inputPanel.setRunning(true);
        statusLabel.setText("Ejecutando...");

        new SwingWorker<ExecutionTrace, Void>() {
            @Override
            protected ExecutionTrace doInBackground() {
                return simulationService.simulate(machine, word, iterations, config.getMaxSteps());
            }

            @Override
            protected void done() {
                inputPanel.setRunning(false);
                try {
                    showTrace(get());
                } catch (ExecutionException anException) {
                    showError(anException.getCause());
                } catch (InterruptedException anException) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }

    private void showTrace(ExecutionTrace trace) {
        traceTableModel.setTrace(trace);
        playback.setTrace(trace);
        if (trace.isTruncated()) {
            JOptionPane.showMessageDialog(this,
                    "La ejecucion supero el limite de " + config.getMaxSteps() + " pasos (" + AppConfig.GUI_MAX_STEPS
                            + ").\nSe muestran solo los primeros pasos.",
                    "Traza incompleta", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showError(Throwable error) {
        statusLabel.setText(playback.hasTrace() ? statusText() : "Escriba una palabra y presione Ejecutar.");
        JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo ejecutar", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates every view with the configuration selected in the playback model.
     */
    private void showCurrentConfiguration() {
        ExecutionTrace trace = playback.getTrace();
        int index = playback.getIndex();
        String tape = trace.tapeAt(index);

        tapePanel.setConfiguration(tape, trace.headPositionAt(index));
        generationsPanel.setGenerations(Rule110TapeReader.generations(tape));
        statusLabel.setText(statusText());

        syncingSelection = true;
        try {
            traceTable.setRowSelectionInterval(index, index);
            traceTable.scrollRectToVisible(traceTable.getCellRect(index, 0, true));
        } finally {
            syncingSelection = false;
        }
    }

    private String statusText() {
        ExecutionTrace trace = playback.getTrace();
        int index = playback.getIndex();
        String state = trace.stateAt(index);
        String tape = trace.tapeAt(index);
        int head = trace.headPositionAt(index);

        StringBuilder text = new StringBuilder("<html>");
        text.append("Paso <b>").append(index).append("</b> de ").append(playback.getLastIndex());
        text.append("&nbsp;&nbsp;&middot;&nbsp;&nbsp;Estado: <b>").append(state).append("</b>");
        text.append("&nbsp;&nbsp;&middot;&nbsp;&nbsp;Cabezal: ").append(head);
        text.append(" (lee <b>").append(tape.charAt(head)).append("</b>)");
        if (machine.getHaltStates().contains(state)) {
            text.append("&nbsp;&nbsp;&middot;&nbsp;&nbsp;<b>Maquina detenida</b>");
        } else if (playback.isAtEnd() && trace.isTruncated()) {
            text.append("&nbsp;&nbsp;&middot;&nbsp;&nbsp;<b>Traza incompleta</b>");
        }
        return text.append("</html>").toString();
    }
}

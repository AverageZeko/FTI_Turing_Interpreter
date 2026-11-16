package turing.ui.gui;

import java.awt.FlowLayout;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Form where the user writes the word and the number of iterations.
 */
class InputPanel extends JPanel {
    private final JTextField wordField = new JTextField("00010011", 24);
    private final JTextField iterationsField = new JTextField("10", 6);
    private final JButton runButton = new JButton("Ejecutar");

    /**
     * @param onRun called with (word, iterations) when the user asks to run the machine
     */
    InputPanel(BiConsumer<String, String> onRun) {
        super(new FlowLayout(FlowLayout.LEFT, 8, 4));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

        wordField.setToolTipText("Cadena binaria de 8 simbolos como minimo");
        iterationsField.setToolTipText("Cantidad de generaciones a calcular, en binario");

        add(new JLabel("Palabra:"));
        add(wordField);
        add(new JLabel("Iteraciones (binario):"));
        add(iterationsField);
        add(runButton);

        Runnable submit = () -> onRun.accept(wordField.getText(), iterationsField.getText());
        runButton.addActionListener(e -> submit.run());
        wordField.addActionListener(e -> submit.run());
        iterationsField.addActionListener(e -> submit.run());
    }

    /**
     * Disables the form while a simulation is running.
     */
    void setRunning(boolean running) {
        wordField.setEnabled(!running);
        iterationsField.setEnabled(!running);
        runButton.setEnabled(!running);
        runButton.setText(running ? "Ejecutando..." : "Ejecutar");
    }
}

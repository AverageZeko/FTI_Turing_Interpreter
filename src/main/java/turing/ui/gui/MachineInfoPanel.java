package turing.ui.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import turing.model.turing_machine.TransitionRule;
import turing.model.turing_machine.TuringMachine;

/**
 * Shows the definition of the loaded machine.
 */
class MachineInfoPanel extends JPanel {
    private int row;

    MachineInfoPanel(TuringMachine machine) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        int transitions = 0;
        for (Map<Character, TransitionRule> rules : machine.getTransitionTable().values()) {
            transitions += rules.size();
        }
        Collection<String> states = machine.getStates();

        addField("Nombre", machine.getName());
        addField("Alfabeto", formatSet(machine.getAlphabet()));
        addField("Espacio vacio", machine.getEmptySpace());
        addField("Estado inicial", machine.getInitialState());
        addField("Estados de parada", formatSet(machine.getHaltStates()));
        addField("Transiciones", String.valueOf(transitions));
        addField("Estados (" + (states == null ? 0 : states.size()) + ")", formatSet(states));

        // pushes the content to the top
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridy = row;
        filler.weighty = 1;
        add(new JPanel(), filler);
    }

    private void addField(String label, String value) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        labelConstraints.insets = new Insets(4, 0, 4, 16);
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(labelComponent.getFont().deriveFont(Font.BOLD));
        add(labelComponent, labelConstraints);

        JTextArea valueComponent = new JTextArea(value);
        valueComponent.setEditable(false);
        valueComponent.setLineWrap(true);
        valueComponent.setWrapStyleWord(true);
        valueComponent.setOpaque(false);
        valueComponent.setBorder(null);
        valueComponent.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        GridBagConstraints valueConstraints = new GridBagConstraints();
        valueConstraints.gridx = 1;
        valueConstraints.gridy = row;
        valueConstraints.weightx = 1;
        valueConstraints.fill = GridBagConstraints.HORIZONTAL;
        valueConstraints.insets = new Insets(4, 0, 4, 0);
        add(valueComponent, valueConstraints);
        row++;
    }

    private static String formatSet(Collection<String> elements) {
        if (elements == null) {
            return "(no definido)";
        }
        return "{" + String.join(", ", elements) + "}";
    }
}

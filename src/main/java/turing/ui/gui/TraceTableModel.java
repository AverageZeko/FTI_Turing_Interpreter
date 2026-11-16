package turing.ui.gui;

import javax.swing.table.AbstractTableModel;

import turing.model.execution.ExecutionTrace;
import turing.model.execution.StepRecord;
import turing.model.turing_machine.TransitionRule;

/**
 * Table with one row per configuration: row 0 is the initial one and row i is the step i.
 */
class TraceTableModel extends AbstractTableModel {
    private static final String[] COLUMNS = { "Paso", "Estado", "Lee", "Escribe", "Operacion", "Siguiente",
            "Cabezal" };

    private ExecutionTrace trace;

    void setTrace(ExecutionTrace newTrace) {
        this.trace = newTrace;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return trace == null ? 0 : trace.configurationCount();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return column == 0 || column == 6 ? Integer.class : String.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row == 0) {
            return switch (column) {
                case 0 -> 0;
                case 1 -> "(inicio)";
                case 5 -> trace.getInitialState();
                case 6 -> trace.getInitialHeadPosition();
                default -> "";
            };
        }
        StepRecord step = trace.getSteps().get(row - 1);
        TransitionRule rule = step.rule();
        return switch (column) {
            case 0 -> step.step();
            case 1 -> step.state();
            case 2 -> String.valueOf(step.readSymbol());
            case 3 -> String.valueOf(rule.getWriteSymbol());
            case 4 -> formatOperation(rule);
            case 5 -> rule.getNextState();
            case 6 -> step.headPosition();
            default -> "";
        };
    }

    /**
     * Formats the move of a rule, adding the target symbol for the macros, e.g. "R* (Δ)".
     */
    private static String formatOperation(TransitionRule rule) {
        if (rule.getTargetSymbol() == null) {
            return rule.getDirection();
        }
        return rule.getDirection() + " (" + rule.getTargetSymbol() + ")";
    }
}

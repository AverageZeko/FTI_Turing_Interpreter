package turing.ui.console;

import java.util.Collection;
import java.util.Set;

import turing.model.execution.StepRecord;
import turing.model.turing_machine.Tape;
import turing.model.turing_machine.TransitionRule;
import turing.model.turing_machine.TuringMachine;

public class ConsoleRenderer {

    /**
     * Prints the machine's definition (name, alphabet, states, initial state and
     * halt states)
     * and the tape, with a mark under the cell where the head is.
     */
    public void printInitialData(TuringMachine turingMachine) {

        Set<String> states = turingMachine.getStates();
        System.out.println("=== Maquina de Turing: " + turingMachine.getName() + " ===");
        printField("Alfabeto", formatSet(turingMachine.getAlphabet()));
        printField("Espacio vacio", "{" + turingMachine.getEmptySpace() + "}");
        printField("Estados (" + (states == null ? 0 : states.size()) + ")", formatSet(states));
        printField("Estado inicial", turingMachine.getInitialState());
        printField("Estados de parada", formatSet(turingMachine.getHaltStates()));
        if (turingMachine.getTape() == null) {
            printField("Cinta", "(no asignada)");
        } else {
            printField("Cinta", tapeWithHead(turingMachine.getTape()) + "   ([ ] = cabezal)");
        }
    }

    /**
     * Prints the result of the execution: final state, number of steps, final tape,
     * the remaining counter and every generation stored in the tape, one per row,
     * so the evolution of the automaton can be read from top to bottom.
     */
    public void printResult(TuringMachine turingMachine) {
        Tape tape = turingMachine.getTape();
        System.out.println();
        System.out.println("=== Resultado ===");
        printField("Estado final", turingMachine.getCurrentState());
        printField("Pasos ejecutados", String.valueOf(turingMachine.getSteps()));
        printField("Cinta final", tapeWithHead(tape));

        String[] blocks = tape.toString().split("#");
        // blocks: [Δ, K, G0, G1, ..., Gt, Δ]
        if (blocks.length > 1) {
            printField("Contador K", blocks[1]);
        }
        int generation = 0;
        for (int i = 2; i < blocks.length; i++) {
            String cells = blocks[i];
            if (cells.matches("[01]+")) {
                printField("G" + generation, cells + "   " + cells.replace('0', '·').replace('1', '█'));
                generation++;
            }
        }
    }

    private static final String STEP_FORMAT = "%6s | %-15s | %-3s | %-7s | %-9s | %-15s | %s%n";

    public void printStepHeader() {
        System.out.println();
        System.out.printf(STEP_FORMAT, "Paso", "Estado", "Lee", "Escribe", "Operacion", "Siguiente",
                "Cinta (despues del paso)");
        System.out.println("-".repeat(100));
    }

    /**
     * Prints one step of the execution as a single row: δ(state, read) = (next,
     * written, operation),
     * followed by the tape after the step with the head's cell between brackets.
     */
    public void printStep(StepRecord r) {
        System.out.printf(STEP_FORMAT, r.step(), r.state(), r.readSymbol(), r.rule().getWriteSymbol(),
                formatOperation(r.rule()),
                r.rule().getNextState(), tapeWithHead(r.tape(), r.headPosition()));
    }

    /**
     * Formats the move of a rule, adding the target symbol for the macros, e.g. "R*
     * (Δ)".
     */
    private static String formatOperation(TransitionRule rule) {
        if (rule.getTargetSymbol() == null) {
            return rule.getDirection();
        }
        return rule.getDirection() + " (" + rule.getTargetSymbol() + ")";
    }

    /**
     * Returns the tape as a string with the cell under the head between brackets,
     * e.g. Δ#10[0]#0001#Δ
     */
    private String tapeWithHead(Tape tape) {
        String content = tape.toString();
        int head = tape.getHeadPosition();
        return content.substring(0, head) + "[" + content.charAt(head) + "]" + content.substring(head + 1);
    }

    /**
     * Returns the tape as a string with the cell under the head between brackets,
     * e.g. Δ#10[0]#0001#Δ
     */
    private String tapeWithHead(String tape, int headPosition) {
        return tape.substring(0, headPosition) + "[" + tape.charAt(headPosition) + "]"
                + tape.substring(headPosition + 1);
    }

    /**
     * Prints a line with the label aligned to a fixed width, so all the values
     * start in the same column.
     */
    private static void printField(String label, String value) {
        System.out.printf("%-20s%s%n", label.isEmpty() ? "" : label + ":", value);
    }

    /**
     * Formats a collection as a set
     */
    private static String formatSet(Collection<String> elements) {
        if (elements == null) {
            return "(no definido)";
        }
        return "{" + String.join(", ", elements) + "}";
    }
}

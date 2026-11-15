package turing.model.turing_machine;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

// Turing machine class
public class TuringMachine {
    private String name;
    private Set<String> alphabet;
    private String emptySpace;
    private Set<String> states;
    private String initialState;
    private final Set<String> haltStates;
    private final Map<String, Map<Character, TransitionRule>> transitionTable;
    private Tape tape;
    private String currentState;

    public TuringMachine(String name, Set<String> alphabet, String emptySpace, Set<String> states, String initialState,
                 Set<String> haltStates, Map<String, Map<Character, TransitionRule>> transitionTable) {
        this.name = name;
        this.alphabet = alphabet;
        this.emptySpace = emptySpace;
        this.states = states;
        this.initialState = initialState;
        this.currentState = initialState;
        this.haltStates = haltStates;
        this.transitionTable = transitionTable;
    }
 
    /**
     * Constructs the tape which will be processed by the machine
     * @param iterations a binary number which represents how many iterations the machine will have
     * @param word a binary sequence which represents the word to be processed by the machine
     */
    public void setTape(String iterations, String word) {
        if (word.length() < 8) {
            throw new IllegalArgumentException("La palabra debe ser un binario de longitud 8 o mayor");
        }
        String newTape = String.format("ΔΔΔ#%s#%s#ΔΔΔ", iterations, word);
        tape = new Tape(newTape);
    }

    /**
     * Prints the machine's definition (name, alphabet, states, initial state and halt states)
     * and the tape, with a mark under the cell where the head is.
     */
    public void printInitialData() {
        System.out.println("=== Maquina de Turing: " + name + " ===");
        printField("Alfabeto", formatSet(alphabet));
        printField("Espacio vacio", "{"+emptySpace+"}");
        printField("Estados (" + (states == null ? 0 : states.size()) + ")", formatSet(states));
        printField("Estado inicial", initialState);
        printField("Estados de parada", formatSet(haltStates));
        if (tape == null) {
            printField("Cinta", "(no asignada)");
        } else {
            printField("Cinta", tape.toString());
            printField("", " ".repeat(tape.getHeadPosition()) + "^ cabezal (posicion " + tape.getHeadPosition() + ")");
        }
    }

    /**
     * Prints a line with the label aligned to a fixed width, so all the values start in the same column.
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

    /**
     * Execute the turing machine based on its map of transitions
     */
    public void run() {
        while (!haltStates.contains(currentState)) {

            Character symbol = tape.read();
            Map<Character, TransitionRule> rules = transitionTable.get(currentState);
            TransitionRule currentRule;
            if (rules != null) {
                currentRule = rules.get(symbol);
            }   else {
                currentRule = null;
            }
            if (currentRule == null) {
                throw new IllegalStateException("No se encontro una transicion para δ(" + currentState + ", " + symbol
                        + ") en la posicion " + tape.getHeadPosition() + ": " + tape);
            }

            tape.write(currentRule.writeSymbol);

            switch (currentRule.direction) {
                case "L":
                    tape.moveLeft();
                    break;
                case "L¬*":
                    tape.moveLeftWhile(currentRule.writeSymbol);
                    break;
                case "L*":
                    tape.moveLeftUntil(currentRule.writeSymbol);
                    break;
                case "R":
                    tape.moveRight();
                    break;
                case "R¬*":
                    tape.moveRightWhile(currentRule.writeSymbol);
                    break;
                case "R*":
                    tape.moveRightUntil(currentRule.writeSymbol);
                    break;
                case "S":
                    break;
                default: 
                    throw new IllegalStateException("Operacion no implementada: " + currentRule.direction);
            }

            currentState = currentRule.nextState;
        }
    }
}
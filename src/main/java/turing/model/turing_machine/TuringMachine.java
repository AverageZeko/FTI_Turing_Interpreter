package turing.model.turing_machine;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import turing.auxiliary.TransitionFunctionsMapper;
import turing.model.data.MachineDefinition;

// Turing machine class
public class TuringMachine {
    private final String name;
    private final Set<String> alphabet;
    private final String emptySpace;
    private final Set<String> states;
    private final String initialState;
    private final Set<String> haltStates;
    private final Map<String, Map<Character, TransitionRule>> transitionTable;
    private Tape tape;
    private String currentState;
    private int steps;

    public int getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public String getEmptySpace() {
        return emptySpace;
    }

    public Set<String> getStates() {
        return states;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getHaltStates() {
        return haltStates;
    }

    public Map<String, Map<Character, TransitionRule>> getTransitionTable() {
        return transitionTable;
    }

    public Tape getTape() {
        return tape;
    }

    public void setTape(Tape tape) {
        this.tape = tape;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    /**
     * Constructor for a turing machine
     * 
     * @param aMachineDefinition the object containing the machine's data
     */
    public TuringMachine(MachineDefinition aMachineDefinition) {
        this.name = aMachineDefinition.getName();
        this.alphabet = aMachineDefinition.getAlphabet();
        this.emptySpace = aMachineDefinition.getEmptySpace();
        this.states = aMachineDefinition.getStates();
        this.initialState = aMachineDefinition.getInitialState();
        this.haltStates = aMachineDefinition.getHaltStates();

        this.currentState = aMachineDefinition.getInitialState();
        this.transitionTable = TransitionFunctionsMapper.mapTransitionFunction(aMachineDefinition);
    }

    /**
     * Constructs the tape which will be processed by the machine
     * 
     * @param word       a binary sequence which represents the word to be processed
     *                   by the machine
     * @param iterations a binary number which represents how many iterations the
     *                   machine will have
     */
    public void setTape(String word, String iterations) {
        if (word.length() < 8) {
            throw new IllegalArgumentException("La palabra debe ser un binario de longitud 8 o mayor");
        }
        String newTape = String.format("Δ#%s#%s#Δ", iterations, word);
        tape = new Tape(newTape);
    }

    /**
     * Prints the machine's definition (name, alphabet, states, initial state and
     * halt states)
     * and the tape, with a mark under the cell where the head is.
     */
    public void printInitialData() {
        System.out.println("=== Maquina de Turing: " + name + " ===");
        printField("Alfabeto", formatSet(alphabet));
        printField("Espacio vacio", "{" + emptySpace + "}");
        printField("Estados (" + (states == null ? 0 : states.size()) + ")", formatSet(states));
        printField("Estado inicial", initialState);
        printField("Estados de parada", formatSet(haltStates));
        if (tape == null) {
            printField("Cinta", "(no asignada)");
        } else {
            printField("Cinta", tapeWithHead() + "   ([ ] = cabezal)");
        }
    }

    /**
     * Prints the result of the execution: final state, number of steps, final tape,
     * the remaining counter and every generation stored in the tape, one per row,
     * so the evolution of the automaton can be read from top to bottom.
     */
    public void printResult() {
        System.out.println();
        System.out.println("=== Resultado ===");
        printField("Estado final", currentState);
        printField("Pasos ejecutados", String.valueOf(steps));
        printField("Cinta final", tapeWithHead());

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

    private static void printStepHeader() {
        System.out.println();
        System.out.printf(STEP_FORMAT, "Paso", "Estado", "Lee", "Escribe", "Operacion", "Siguiente", "Cinta (despues del paso)");
        System.out.println("-".repeat(100));
    }

    /**
     * Prints one step of the execution as a single row: δ(state, read) = (next, written, operation),
     * followed by the tape after the step with the head's cell between brackets.
     */
    private void printStep(String state, Character read, TransitionRule rule) {
        System.out.printf(STEP_FORMAT, steps, state, read, rule.getWriteSymbol(), formatOperation(rule),
                rule.getNextState(), tapeWithHead());
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

    /**
     * Returns the tape as a string with the cell under the head between brackets, e.g. Δ#10[0]#0001#Δ
     */
    private String tapeWithHead() {
        String content = tape.toString();
        int head = tape.getHeadPosition();
        return content.substring(0, head) + "[" + content.charAt(head) + "]" + content.substring(head + 1);
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

    /**
     * Execute the turing machine based on its map of transitions
     */
    public void run() {
        steps = 0;
        printStepHeader();
        while (!haltStates.contains(currentState)) {

            Character symbol = tape.read();
            Map<Character, TransitionRule> rules = transitionTable.get(currentState);
            TransitionRule currentRule;
            if (rules != null) {
                currentRule = rules.get(symbol);
            } else {
                currentRule = null;
            }
            if (currentRule == null) {
                throw new IllegalStateException("No se encontro una transicion para δ(" + currentState + ", " + symbol
                        + ") en la posicion " + tape.getHeadPosition() + ": " + tape);
            }
            String previousState = currentState;
            tape.write(currentRule.getWriteSymbol());

            switch (currentRule.getDirection()) {
                case "L":
                    tape.moveLeft();
                    break;
                case "L¬*":
                    tape.moveLeftWhile(currentRule.getTargetSymbol());
                    break;
                case "L*":
                    tape.moveLeftUntil(currentRule.getTargetSymbol());
                    break;
                case "SL":
                    tape.shiftLeft();
                    break;
                case "R":
                    tape.moveRight();
                    break;
                case "R¬*":
                    tape.moveRightWhile(currentRule.getTargetSymbol());
                    break;
                case "R*":
                    tape.moveRightUntil(currentRule.getTargetSymbol());
                    break;
                case "SR":
                    tape.shiftRight();
                    break;
                case "S":
                    break;
                default:
                    throw new IllegalStateException("Operacion no implementada: " + currentRule.getDirection());
            }

            currentState = currentRule.getNextState();
            steps++;
            printStep(previousState, symbol, currentRule);
        }

    }
}
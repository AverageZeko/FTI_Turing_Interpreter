package turing.model.turing_machine;

import java.util.Map;
import java.util.Set;


// Turing machine class
public class TuringMachine {
    private final Tape tape;
    private final Map<String, Map<Character, TransitionRule>> transitionTable;
    private final Set<String> haltStates;
    private String currentState;

    public TuringMachine(String initialState, Set<String> haltStates, Map<String, Map<Character, TransitionRule>> transitionFunctions, String iterations, String word) {
        if (word.length() < 8) {
            throw new IllegalArgumentException("La palabra debe ser un binario de longitud 8 o mayor");
        }
        String newTape = String.format("ΔΔΔ#%s#%s#ΔΔΔ", iterations, word);
        tape = new Tape(newTape);

        currentState = initialState;
        this.haltStates = haltStates;
        transitionTable = transitionFunctions;
    }
 
    // Run the Turing machine
    public void run() {
    }
}
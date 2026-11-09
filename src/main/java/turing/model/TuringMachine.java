package turing.model;

import java.util.Map;
 
// Turing machine class
public class TuringMachine {
    private Tape tape;
    private State currentState;
    private Map<String, Map<Character, TransitionRule>> transitionTable;
 
    public TuringMachine(Map<String, Map<Character, TransitionRule>> transitionFunctions, String iterations, String word) {

        if (word.length() < 8) {
            throw new IllegalArgumentException("La palabra debe ser un binario de longitud 8 o mayor");
        }
        String newTape = String.format("ΔΔΔ#%s#%s#ΔΔΔ", iterations, word);
        tape = new Tape(newTape);

        currentState = State.Q0;
        transitionTable = transitionFunctions;
    }
 
    // Run the Turing machine
    public void run() {
/*         while (currentState != State.HALT) {
            char currentSymbol = tape.read();
            TransitionRule rule = transitionTable.get(currentState).get(currentSymbol);
            if (rule == null) {
                System.out.println("No transition rule found. Halting.");
                break;
            }
            tape.write(rule.writeSymbol);
            if (rule.direction == 'L') {
                tape.moveLeft();
            } else {
                tape.moveRight();
            }
            currentState = rule.nextState;
        } */
    }
}
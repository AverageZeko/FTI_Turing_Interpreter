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
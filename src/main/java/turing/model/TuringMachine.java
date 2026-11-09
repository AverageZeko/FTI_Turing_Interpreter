package turing.model;

import java.util.HashMap;
import java.util.Map;
 
// Turing machine class
public class TuringMachine {
    private Tape tape;
    private State currentState;
    private Map<State, Map<Character, TransitionRule>> transitionTable;
 
    public TuringMachine() {
        tape = new Tape();
        currentState = State.Q0;
        transitionTable = new HashMap<>();
 
        // Initialize the transition table
        Map<Character, TransitionRule> q0Rules = new HashMap<>();
        q0Rules.put('0', new TransitionRule(State.Q1, '1', 'R'));
        transitionTable.put(State.Q0, q0Rules);
 
        Map<Character, TransitionRule> q1Rules = new HashMap<>();
        q1Rules.put('1', new TransitionRule(State.HALT, '0', 'L'));
        transitionTable.put(State.Q1, q1Rules);
    }
 
    // Run the Turing machine
    public void run() {
        while (currentState != State.HALT) {
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
        }
    }
}
package turing.model;

// Transition rule class to store a single transition
public class TransitionRule {
    String nextState;  // The next state to go to
    char writeSymbol; // The symbol to be written
    char direction; // 'L' for left, 'R' for right and 'S' for no movement (stand)
 
    public TransitionRule(String nextState, char writeSymbol, char direction) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
    }
}
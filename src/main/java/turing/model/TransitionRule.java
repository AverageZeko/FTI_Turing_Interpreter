package turing.model;

// Transition rule class to store a single transition
public class TransitionRule {
    State nextState;
    char writeSymbol;
    char direction; // 'L' for left, 'R' for right
 
    public TransitionRule(State nextState, char writeSymbol, char direction) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
    }
}
package turing.model.turing_machine;

// Transition rule class to store a single transition
public class TransitionRule {
    String nextState;  // The next state to go to
    char writeSymbol; // The symbol to be written
    
    /**
     * The operation to be made by the machine's tape
     * <p> L   : Move left
     * <p> L*  : Move leftuntil coming across the symbol written
     * <p> L¬* : Move left while coming across the symbol written
     * <p> R   : Move right
     * <p> R*  : Move right until coming across the symbol written
     * <p> R¬* : Move right while coming across the symbol written
     * <p> S   : Stay
     */
    String direction;
 
    public TransitionRule(String nextState, char writeSymbol, String direction) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
    }
}
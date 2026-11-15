package turing.model.turing_machine;

// Transition rule class to store a single transition
public class TransitionRule {
    /**
     * The state to go after writting
     */
    String nextState;

    /**
     * The symbol to be written
     */
    Character writeSymbol;
    
    /**
     * The symbol to be used for (L*, R*, L¬*, R¬*) operations
     */
    Character targetSymbol;
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
 
    public TransitionRule(String nextState, Character writeSymbol, Character targetSymbol, String direction) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.targetSymbol = targetSymbol;
        this.direction = direction;
    }
}
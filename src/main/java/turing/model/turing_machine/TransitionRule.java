package turing.model.turing_machine;

// Transition rule class to store a single transition
public class TransitionRule {
    /**
     * The state to go after writting
     */
    private final String nextState;

    /**
     * The symbol to be written
     */
    private final Character writeSymbol;
    
    /**
     * The symbol to be used for (L*, R*, L¬*, R¬*) operations
     */
    private final Character targetSymbol;
    /**
     * The operation to be made by the machine's tape
     * <p> L   : Move left
     * <p> L*  : Move left until coming across the target symbol
     * <p> L¬* : Move left while coming across the target symbol
     * <p> R   : Move right
     * <p> R*  : Move right until coming across the target symbol
     * <p> R¬* : Move right while coming across the target symbol
     * <p> S   : Stay
     */
    private final String direction;
 
    public TransitionRule(String nextState, Character writeSymbol, Character targetSymbol, String direction) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.targetSymbol = targetSymbol;
        this.direction = direction;
    }

	public String getNextState() {
		return nextState;
	}

	public Character getWriteSymbol() {
		return writeSymbol;
	}

	public Character getTargetSymbol() {
		return targetSymbol;
	}

	public String getDirection() {
		return direction;
	}

}
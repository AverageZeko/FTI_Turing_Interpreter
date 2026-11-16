package turing.model.turing_machine;

import java.util.Map;
import java.util.Set;

import turing.auxiliary.TransitionFunctionsMapper;
import turing.model.data.MachineDefinition;
import turing.model.execution.ExecutionTrace;
import turing.model.execution.StepRecord;

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


    public ExecutionTrace run() {

        ExecutionTrace trace = new ExecutionTrace();
        while (!isHalted())
            trace.addStep(step());
        return trace;
    }

    public StepRecord step() {

        if (tape == null)
            throw new IllegalStateException("La cinta no fue asignada");
        if (isHalted())
            throw new IllegalStateException("La maquina ya se detuvo en " + currentState);

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
        StepRecord stepRecord = new StepRecord(steps, previousState, symbol, currentRule, tape.toString(),
                tape.getHeadPosition());
        return stepRecord;
    }

    public boolean isHalted() {
        return haltStates.contains(currentState);
    }

    public void reset(Tape newTape) {
        this.tape = newTape;
        this.currentState = initialState;
        this.steps = 0;
    }

}
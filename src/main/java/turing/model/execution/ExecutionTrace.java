package turing.model.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Record of a whole execution: the initial configuration and every step applied after it.
 * <p>
 * Configuration {@code i} is the initial one when {@code i == 0}, and the one right after
 * step {@code i} otherwise, so there are {@code getSteps().size() + 1} configurations.
 */
public class ExecutionTrace {
    private final String initialState;
    private final String initialTape;
    private final int initialHeadPosition;
    private final List<StepRecord> stepsRecords;
    private boolean truncated;

    public ExecutionTrace(String initialState, String initialTape, int initialHeadPosition) {
        this.initialState = initialState;
        this.initialTape = initialTape;
        this.initialHeadPosition = initialHeadPosition;
        this.stepsRecords = new ArrayList<>();
    }

    public void addStep(StepRecord sr) {
        this.stepsRecords.add(sr);
    }

    public List<StepRecord> getSteps() {
        return Collections.unmodifiableList(stepsRecords);
    }

    /**
     * @return the number of configurations (initial one included)
     */
    public int configurationCount() {
        return stepsRecords.size() + 1;
    }

    /**
     * @param index 0 for the initial configuration, i for the configuration after step i
     * @return the tape at that configuration
     */
    public String tapeAt(int index) {
        return index == 0 ? initialTape : stepsRecords.get(index - 1).tape();
    }

    /**
     * @param index 0 for the initial configuration, i for the configuration after step i
     * @return the head position at that configuration
     */
    public int headPositionAt(int index) {
        return index == 0 ? initialHeadPosition : stepsRecords.get(index - 1).headPosition();
    }

    /**
     * @param index 0 for the initial configuration, i for the configuration after step i
     * @return the state the machine is in at that configuration
     */
    public String stateAt(int index) {
        return index == 0 ? initialState : stepsRecords.get(index - 1).rule().getNextState();
    }

    public String getInitialState() {
        return initialState;
    }

    public String getInitialTape() {
        return initialTape;
    }

    public int getInitialHeadPosition() {
        return initialHeadPosition;
    }

    /**
     * @return true if the execution was stopped by the steps limit before reaching a halt state
     */
    public boolean isTruncated() {
        return truncated;
    }

    public void markTruncated() {
        this.truncated = true;
    }
}

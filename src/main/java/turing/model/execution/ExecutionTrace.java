package turing.model.execution;

import java.util.ArrayList;
import java.util.List;

public class ExecutionTrace {
    private final List<StepRecord> stepsRecords;

    public ExecutionTrace() {
        stepsRecords = new ArrayList<>();
    }

    public void addStep(StepRecord sr) {
        this.stepsRecords.add(sr);
    }

    public List<StepRecord> getSteps() {
        return stepsRecords;
    }

}

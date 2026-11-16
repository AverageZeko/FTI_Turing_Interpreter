package turing.model.execution;

import java.util.ArrayList;
import java.util.Collection;

public class ExecutionTrace {
    Collection<StepRecord> stepsRecords;

    public ExecutionTrace() {
        stepsRecords = new ArrayList<>();
    }

    public void addStep(StepRecord sr) {
        if (stepsRecords != null)
            this.stepsRecords.add(sr);
    }

    public Collection<StepRecord> getSteps() {
        return stepsRecords;
    }

    public void initialConfig() {

    }
}

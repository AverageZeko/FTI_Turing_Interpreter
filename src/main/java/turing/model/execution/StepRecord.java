package turing.model.execution;

import turing.model.turing_machine.Tape;
import turing.model.turing_machine.TransitionRule;

public record StepRecord(int step, String state, Character readSymbol, TransitionRule rule, String tape,
        int headPosition) {

}

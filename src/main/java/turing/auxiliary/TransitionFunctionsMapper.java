package turing.auxiliary;

import java.util.HashMap;
import java.util.Map;

import turing.model.data.MachineDefinition;
import turing.model.dto.TransitionDto;
import turing.model.turing_machine.TransitionRule;

public class TransitionFunctionsMapper {

    private TransitionFunctionsMapper() {
    }

    /**
     * Converts the list of transitions of a machine definition into the transition table
     * used by the Turing machine: state -> (symbol read -> transition rule).
     * @param aMachineDefinition the machine definition read from the JSON file
     * @return the transition table
     * @throws IllegalArgumentException if two transitions have the same state and symbol read
     */
    public static Map<String, Map<Character, TransitionRule>> mapTransitionFunction(MachineDefinition aMachineDefinition) {
        Map<String, Map<Character, TransitionRule>> result = new HashMap<>();

        for (TransitionDto currentData : aMachineDefinition.getTransitions()) {
            Character characterRead = currentData.getRead();
            TransitionRule aTransitionRule = new TransitionRule(
                    currentData.getNext(), currentData.getWrite(), currentData.getMove());

            Map<Character, TransitionRule> stateRules = result.computeIfAbsent(currentData.getState(), state -> new HashMap<>());
            if (stateRules.containsKey(characterRead)) {
                throw new IllegalArgumentException("Transicion duplicada δ(" + currentData.getState() + ", " + characterRead + ")");
            }
            stateRules.put(characterRead, aTransitionRule);
        }
        return result;
    }
}

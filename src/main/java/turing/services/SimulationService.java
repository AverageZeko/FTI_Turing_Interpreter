package turing.services;

import turing.auxiliary.JsonHelper;
import turing.model.execution.ExecutionTrace;
import turing.model.turing_machine.Tape;
import turing.model.turing_machine.TuringMachine;

public class SimulationService {

    /**
     * Loads a machine stored in src/main/resources/rules.
     * @param ruleName the file name without the ".json" extension, e.g. "rule_110_trace"
     * @return a new machine in its initial state
     */
    public TuringMachine loadMachine(String ruleName) {
        return new TuringMachine(JsonHelper.fetchMachineFromResource(ruleName));
    }

    /**
     * Constructs the tape which will be processed by the machine
     *
     * @param word       a binary sequence which represents the word to be processed
     *                   by the machine
     * @param iterations a binary number which represents how many iterations the
     *                   machine will have
     */
    public Tape setTape(String word, String iterations) {
        if (word.length() < 8) {
            throw new IllegalArgumentException("La palabra debe ser un binario de longitud 8 o mayor");
        }
        if (!word.matches("[01]+")) {
            throw new IllegalArgumentException("La palabra debe ser un binario");
        }
        if (!iterations.matches("[01]+")) {
            throw new IllegalArgumentException("Las iteraciones deben ser un numero binario");
        }

        String newTape = String.format("Δ#%s#%s#Δ", iterations, word);
        Tape tape = new Tape(newTape);
        return tape;
    }

    /**
     * Validates the input, resets the machine with a new tape and executes it.
     * @param machine    the machine to execute
     * @param word       the binary word to process
     * @param iterations the binary number of iterations
     * @param maxSteps   the maximum number of steps to record
     * @return the trace of the execution
     */
    public ExecutionTrace simulate(TuringMachine machine, String word, String iterations, int maxSteps) {
        machine.reset(setTape(word.trim(), iterations.trim()));
        return machine.run(maxSteps);
    }
}

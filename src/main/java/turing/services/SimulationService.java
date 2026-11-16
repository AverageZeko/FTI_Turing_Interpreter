package turing.services;

import turing.model.turing_machine.Tape;

public class SimulationService {
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
            throw new IllegalArgumentException("La palabra debe ser una binario");
        }
        if (!iterations.matches("[01]+")) {
            throw new IllegalArgumentException("Las iteraciones deben ser un numero binario");
        }

        String newTape = String.format("Δ#%s#%s#Δ", iterations, word);
        Tape tape = new Tape(newTape);
        return tape;
    }
}

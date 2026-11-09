package turing.model;

import java.util.ArrayList;
 
// Tape class to represent the Turing machine tape
public class Tape {
    private ArrayList<Character> tape;
    private int headPosition;
 
    /**
     * Constructor for the machine's tape
     * @param newTape the tape to be assigned, must have format like ΔΔΔ#s#s#ΔΔΔ where s is a binary 
     */
    public Tape(String newTape) {
        tape = new ArrayList<>();
        for (Character currentSymbol : newTape.toCharArray()) {
            tape.add(currentSymbol);
        }
        headPosition = 0;
    }
 
    public int getHeadPosition() {
        return headPosition;
    }

    /**
     *  Reads the symbol at the current head position
     * @return the symbol being read
     */
    public Character read() {
        return tape.get(headPosition);
    }
 
    /**
     *  Write a symbol at the current head position
     * 
     * @param symbol the symbol to be written in the tape's current position
     */
    public void write(Character symbol) {
        tape.set(headPosition, symbol);
    }
 
    /**
     * Move the head left, regardless of the current symbol in the tape
     */
    public void moveLeft() {
        headPosition--;
    }

    /**
     * Move the head left while the characterTarget is not equal to the current symbol in the tape
     * @param characterTarget the symbol where the head will stop
     */
    public void moveLeftUntil(Character characterTarget) {
        headPosition--;
        while (!tape.get(headPosition).equals(characterTarget)) {
            headPosition--;
        }
    }

    /**
     * Move the head left while the characterTarget is equal to the current symbol in the tape
     * @param characterTarget
     */
    public void moveLeftWhile(Character characterTarget) {
        headPosition--;
        while (tape.get(headPosition).equals(characterTarget)) {
            headPosition--;
        }
    }
 
    /**
     * Move the head right, regardless of the current symbol in the tape
     */
    public void moveRight() {
        headPosition++;
    }

    /**
     * Move the head right while the characterTarget is not equal to the current symbol in the tape
     * @param characterTarget the symbol where the head will stop
     */
    public void moveRightUntil(Character characterTarget) {
        headPosition++;
        while (!tape.get(headPosition).equals(characterTarget)) {
            headPosition++;
        }
    }

    /**
     * Move the head right while the characterTarget is equal to the current symbol in the tape
     * @param characterTarget
     */
    public void moveRightWhile(Character characterTarget) {
        headPosition++;
        while (tape.get(headPosition).equals(characterTarget)) {
            headPosition++;
        }
    }
}
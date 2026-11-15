package turing.model.turing_machine;

import java.util.ArrayList;
 
// Tape class to represent the Turing machine tape
public class Tape {
    public static final Character EMPTY_SPACE = 'Δ';
    private ArrayList<Character> tape;
    private int headPosition;
 
    /**
     * Constructor for the machine's tape.
     * @param newTape the tape to be assigned, must have format like ΔΔΔ#s#s#ΔΔΔ where s is a binary.
     */
    public Tape(String newTape) {
        tape = new ArrayList<>();
        for (Character currentSymbol : newTape.toCharArray()) {
            tape.add(currentSymbol);
        }
        headPosition = 0;
    }
 
    /**
     * Returns the content of the tape as a string, e.g. ΔΔΔ#100#00010010#ΔΔΔ
     */
    @Override
    public String toString() {
        StringBuilder content = new StringBuilder(tape.size());
        for (Character symbol : tape) {
            content.append(symbol);
        }
        return content.toString();
    }

    /**
     * Return the current position of the tape's head.
     * @return current value of headPosition.
     */
    public int getHeadPosition() {
        return headPosition;
    }

    /**
     * Reads the symbol at the current head position.
     * @return the symbol being read.
     */
    public Character read() {
        return tape.get(headPosition);
    }
 
    /**
     * Write a symbol at the head's current position.
     * @param symbol the symbol to be written in the tape's current position.
     */
    public void write(Character symbol) {
        tape.set(headPosition, symbol);
    }
 
    /**
     * Move the head left, regardless of the current symbol in the tape.
     * If the head is on the first cell, a blank is inserted at the beginning of the tape.
     */
    public void moveLeft() {
        headPosition--;
        if (headPosition == 0) {
            tape.addFirst(EMPTY_SPACE);
        }
    }

    /**
     * Move the head left while the characterTarget is not equal to the current symbol in the tape.
     * @param characterTarget the symbol where the head will stop.
     */
    public void moveLeftUntil(Character characterTarget) {
        moveLeft();
        while (!read().equals(characterTarget)) {
            moveLeft();
        }
    }

    /**
     * Move the head left while the characterTarget is equal to the current symbol in the tape.
     * @param characterTarget the symbol to skip over.
     */
    public void moveLeftWhile(Character characterTarget) {
        moveLeft();
        while (read().equals(characterTarget)) {
            moveLeft();
        }
    }

    /**
     * Move the head right, regardless of the current symbol in the tape.
     * If the head moves past the last cell, a blank is inserted at the end of the tape.
     */
    public void moveRight() {
        headPosition++;
        if (headPosition == tape.size()) {
            tape.addLast(EMPTY_SPACE);
        }
    }

    /**
     * Move the head right while the characterTarget is not equal to the current symbol in the tape.
     * @param characterTarget the symbol where the head will stop.
     */
    public void moveRightUntil(Character characterTarget) {
        moveRight();
        while (!read().equals(characterTarget)) {
            moveRight();
        }
    }

    /**
     * Move the head right while the characterTarget is equal to the current symbol in the tape.
     * @param characterTarget the symbol to skip over
     */
    public void moveRightWhile(Character characterTarget) {
        moveRight();
        while (read().equals(characterTarget)) {
            moveRight();
        }
    }

    /**
     * Shifts the block of symbols to the right of the head one position to the left
     */
    public void shiftLeft() {
        int end = findBlankRightOf(headPosition + 1);
        for (int i = headPosition; i < end - 1; i++) {
            tape.set(i, tape.get(i + 1));
        }
        tape.set(end - 1, EMPTY_SPACE);
    }

    /**
     * Shifts the block of symbols to the left of the head one position to the right
     */
    public void shiftRight() {
        int end = findBlankRightOf(headPosition);
        if (end == tape.size()) {
            tape.add(EMPTY_SPACE);
        }
        for (int i = end; i > headPosition; i--) {
            tape.set(i, tape.get(i - 1));
        }
        tape.set(headPosition, EMPTY_SPACE);
    }

    private int findBlankRightOf(int from) {
        int i = from;
        while (i < tape.size() && !tape.get(i).equals(EMPTY_SPACE)) {
            i++;
        }
        return i;
    }
}
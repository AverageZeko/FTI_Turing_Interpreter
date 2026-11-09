package turing.model;

import java.util.ArrayList;
 
// Tape class to represent the Turing machine tape
public class Tape {
    private ArrayList<Character> tape;
    private int headPosition;
 
    public Tape() {
        tape = new ArrayList<>();
        tape.add('Δ'); // Initialize the tape with a blank symbol
        headPosition = 0;
    }
 
    // Read the symbol at the current head position
    public char read() {
        return tape.get(headPosition);
    }
 
    // Write a symbol at the current head position
    public void write(char symbol) {
        tape.set(headPosition, symbol);
    }
 
    // Move the head left
    public void moveLeft() {
        if (headPosition == 0) {
            tape.add(0, 'Δ'); // Add a blank symbol at the beginning if needed
        } else {
            headPosition--;
        }
    }
 
    // Move the head right
    public void moveRight() {
        headPosition++;
        if (headPosition == tape.size()) {
            tape.add('Δ'); // Add a blank symbol at the end if needed
        }
    }
}
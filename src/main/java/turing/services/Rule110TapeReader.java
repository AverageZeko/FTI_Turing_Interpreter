package turing.services;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the blocks of a Rule 110 tape, which has the format Δ#K#G0#G1#...#Gt#Δ,
 * where K is the remaining iterations counter and each Gi is a generation.
 */
public final class Rule110TapeReader {

    private static final String SEPARATOR = "#";
    private static final String BINARY = "[01]+";

    private Rule110TapeReader() {
    }

    /**
     * @param tape the content of the tape
     * @return the iterations counter K, or null if the tape has no such block
     */
    public static String counter(String tape) {
        String[] blocks = tape.split(SEPARATOR);
        return blocks.length > 1 ? blocks[1] : null;
    }

    /**
     * Returns every complete generation stored in the tape, in order.
     * Blocks that are being rewritten (they contain marks like X or Y) are skipped.
     * @param tape the content of the tape
     * @return the generations, the first one is the initial word
     */
    public static List<String> generations(String tape) {
        String[] blocks = tape.split(SEPARATOR);
        // blocks: [Δ, K, G0, G1, ..., Gt, Δ]
        List<String> result = new ArrayList<>();
        for (int i = 2; i < blocks.length; i++) {
            if (blocks[i].matches(BINARY)) {
                result.add(blocks[i]);
            }
        }
        return result;
    }
}

package turing.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import turing.model.execution.ExecutionTrace;
import turing.model.turing_machine.TuringMachine;

class SimulationServiceTest {

    private final SimulationService service = new SimulationService();

    @Test
    void rejectsShortWord() {
        assertThrows(IllegalArgumentException.class, () -> service.setTape("0101", "1"));
    }

    @Test
    void rejectsNonBinaryWord() {
        assertThrows(IllegalArgumentException.class, () -> service.setTape("0101abcd", "1"));
    }

    @Test
    void rejectsNonBinaryOrEmptyIterations() {
        assertThrows(IllegalArgumentException.class, () -> service.setTape("00010011", "3"));
        assertThrows(IllegalArgumentException.class, () -> service.setTape("00010011", ""));
    }

    @Test
    void simulateRecordsInitialConfigurationAndEverySteps() {
        TuringMachine machine = service.loadMachine("rule_110_trace");

        ExecutionTrace trace = service.simulate(machine, "00010011", "10", 1_000_000);

        assertFalse(trace.isTruncated());
        assertEquals(189, trace.getSteps().size());
        assertEquals(190, trace.configurationCount());
        assertEquals("Δ#10#00010011#Δ", trace.tapeAt(0));
        assertEquals("q_start", trace.stateAt(0));
        assertEquals("HALT", trace.stateAt(trace.configurationCount() - 1));
        assertEquals(List.of("00010011", "00110111", "01111101"),
                Rule110TapeReader.generations(trace.tapeAt(trace.configurationCount() - 1)));
    }

    @Test
    void simulateStopsAtStepLimit() {
        TuringMachine machine = service.loadMachine("rule_110_trace");

        ExecutionTrace trace = service.simulate(machine, "00010011", "10", 20);

        assertTrue(trace.isTruncated());
        assertEquals(20, trace.getSteps().size());
    }

    @Test
    void machineCanBeReusedForANewSimulation() {
        TuringMachine machine = service.loadMachine("rule_110_trace");
        service.simulate(machine, "00010011", "10", 1_000_000);

        ExecutionTrace second = service.simulate(machine, "00010011", "0", 1_000_000);

        assertEquals(5, second.getSteps().size());
        assertEquals(1, second.getSteps().get(0).step());
    }

    @Test
    void tapeReaderSkipsBlocksBeingRewritten() {
        assertEquals("01", Rule110TapeReader.counter("Δ#01#00010011#00X1Y#Δ"));
        assertEquals(List.of("00010011"), Rule110TapeReader.generations("Δ#01#00010011#00X1Y#Δ"));
    }
}

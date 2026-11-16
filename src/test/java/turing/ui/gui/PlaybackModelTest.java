package turing.ui.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import turing.model.execution.ExecutionTrace;
import turing.services.SimulationService;

class PlaybackModelTest {

    private static ExecutionTrace sampleTrace() {
        SimulationService service = new SimulationService();
        // 5 steps -> 6 configurations
        return service.simulate(service.loadMachine("rule_110_trace"), "00010011", "0", 1000);
    }

    @Test
    void indexIsClampedToTheTrace() {
        PlaybackModel model = new PlaybackModel();
        model.setTrace(sampleTrace());

        model.setIndex(100);
        assertEquals(5, model.getIndex());
        assertTrue(model.isAtEnd());

        model.setIndex(-3);
        assertEquals(0, model.getIndex());
        assertTrue(model.isAtStart());
    }

    @Test
    void listenersAreNotifiedOnlyWhenTheIndexChanges() {
        PlaybackModel model = new PlaybackModel();
        AtomicInteger notifications = new AtomicInteger();
        model.addListener(notifications::incrementAndGet);

        model.setTrace(sampleTrace());
        model.setIndex(0);
        model.next();
        model.next();
        model.previous();

        assertEquals(4, notifications.get());
        assertEquals(1, model.getIndex());
    }

    @Test
    void withoutTraceNavigationDoesNothing() {
        PlaybackModel model = new PlaybackModel();

        model.next();
        model.last();

        assertEquals(0, model.getIndex());
    }
}

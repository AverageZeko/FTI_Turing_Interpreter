package turing.ui.gui;

import java.util.ArrayList;
import java.util.List;

import turing.model.execution.ExecutionTrace;

/**
 * Holds the trace being shown and the configuration currently selected.
 * Every view listens to this model, so they all stay in sync.
 */
class PlaybackModel {
    private final List<Runnable> listeners = new ArrayList<>();
    private ExecutionTrace trace;
    private int index;

    void addListener(Runnable listener) {
        listeners.add(listener);
    }

    void setTrace(ExecutionTrace newTrace) {
        this.trace = newTrace;
        this.index = 0;
        fireChanged();
    }

    ExecutionTrace getTrace() {
        return trace;
    }

    boolean hasTrace() {
        return trace != null;
    }

    int getIndex() {
        return index;
    }

    /**
     * @return the index of the last configuration, or 0 when there is no trace
     */
    int getLastIndex() {
        return trace == null ? 0 : trace.configurationCount() - 1;
    }

    boolean isAtStart() {
        return index == 0;
    }

    boolean isAtEnd() {
        return index == getLastIndex();
    }

    /**
     * Selects a configuration, clamping the index to the valid range.
     * Listeners are notified only if the index actually changes.
     */
    void setIndex(int newIndex) {
        if (trace == null) {
            return;
        }
        int clamped = Math.max(0, Math.min(newIndex, getLastIndex()));
        if (clamped != index) {
            index = clamped;
            fireChanged();
        }
    }

    void next() {
        setIndex(index + 1);
    }

    void previous() {
        setIndex(index - 1);
    }

    void first() {
        setIndex(0);
    }

    void last() {
        setIndex(getLastIndex());
    }

    private void fireChanged() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
}

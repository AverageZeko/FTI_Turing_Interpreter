package turing.ui.gui;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

/**
 * Buttons to move through the trace and to play it automatically at a chosen speed.
 */
class PlaybackControls extends JPanel {
    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 500;
    private static final int INITIAL_SPEED = 10;
    private static final int MIN_DELAY_MS = 16;

    private final PlaybackModel model;
    private final JButton firstButton = new JButton("Inicio", PlaybackIcon.FIRST);
    private final JButton previousButton = new JButton("Anterior", PlaybackIcon.PREVIOUS);
    private final JButton playButton = new JButton("Reproducir", PlaybackIcon.PLAY);
    private final JButton nextButton = new JButton("Siguiente", PlaybackIcon.NEXT);
    private final JButton lastButton = new JButton("Fin", PlaybackIcon.LAST);
    private final JSlider speedSlider = new JSlider(MIN_SPEED, MAX_SPEED, INITIAL_SPEED);
    private final JLabel speedLabel = new JLabel();
    private final Timer timer;
    private int stepsPerTick = 1;

    PlaybackControls(PlaybackModel model) {
        super(new FlowLayout(FlowLayout.CENTER, 8, 6));
        this.model = model;
        setBorder(BorderFactory.createEmptyBorder(0, 6, 4, 6));

        timer = new Timer(100, e -> tick());
        applySpeed();

        firstButton.setToolTipText("Ir a la configuracion inicial");
        previousButton.setToolTipText("Paso anterior");
        nextButton.setToolTipText("Paso siguiente");
        lastButton.setToolTipText("Ir a la configuracion final");
        speedSlider.setToolTipText("Pasos por segundo durante la reproduccion");

        firstButton.addActionListener(e -> { stop(); model.first(); });
        previousButton.addActionListener(e -> { stop(); model.previous(); });
        nextButton.addActionListener(e -> { stop(); model.next(); });
        lastButton.addActionListener(e -> { stop(); model.last(); });
        playButton.addActionListener(e -> togglePlay());
        speedSlider.addChangeListener(e -> applySpeed());

        // the icon goes after the text on the "forward" buttons
        nextButton.setHorizontalTextPosition(JButton.LEADING);
        lastButton.setHorizontalTextPosition(JButton.LEADING);

        add(firstButton);
        add(previousButton);
        add(playButton);
        add(nextButton);
        add(lastButton);
        add(new JLabel("   Velocidad:"));
        add(speedSlider);
        add(speedLabel);

        model.addListener(this::updateButtons);
        updateButtons();
    }

    private void togglePlay() {
        if (timer.isRunning()) {
            stop();
            return;
        }
        if (model.isAtEnd()) {
            model.first();
        }
        timer.start();
        updateButtons();
    }

    void stop() {
        timer.stop();
        updateButtons();
    }

    private void tick() {
        model.setIndex(model.getIndex() + stepsPerTick);
        if (model.isAtEnd()) {
            stop();
        }
    }

    /**
     * Converts the steps per second of the slider into a timer delay. Above ~60 steps per second
     * the timer cannot fire fast enough, so several steps are advanced on each tick.
     */
    private void applySpeed() {
        int speed = speedSlider.getValue();
        int delay = Math.max(MIN_DELAY_MS, 1000 / speed);
        stepsPerTick = Math.max(1, Math.round(speed * delay / 1000f));
        timer.setDelay(delay);
        speedLabel.setText(speed + " pasos/s");
    }

    private void updateButtons() {
        boolean hasTrace = model.hasTrace();
        firstButton.setEnabled(hasTrace && !model.isAtStart());
        previousButton.setEnabled(hasTrace && !model.isAtStart());
        nextButton.setEnabled(hasTrace && !model.isAtEnd());
        lastButton.setEnabled(hasTrace && !model.isAtEnd());
        playButton.setEnabled(hasTrace && model.getLastIndex() > 0);
        playButton.setText(timer.isRunning() ? "Pausa" : "Reproducir");
        playButton.setIcon(timer.isRunning() ? PlaybackIcon.PAUSE : PlaybackIcon.PLAY);
    }
}

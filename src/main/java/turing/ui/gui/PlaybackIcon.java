package turing.ui.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * Small playback icons drawn with shapes, so they do not depend on the glyphs of the font.
 */
enum PlaybackIcon implements Icon {
    FIRST, PREVIOUS, PLAY, PAUSE, NEXT, LAST;

    private static final int SIZE = 12;

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(component.isEnabled() ? component.getForeground() : UIManager.getColor("Label.disabledForeground"));
        g.translate(x, y);
        switch (this) {
            case FIRST -> {
                g.fillRect(1, 1, 2, SIZE - 2);
                leftTriangle(g, 3, SIZE - 1);
            }
            case PREVIOUS -> leftTriangle(g, 1, SIZE - 2);
            case PLAY -> rightTriangle(g, 2, SIZE - 1);
            case PAUSE -> {
                g.fillRect(2, 1, 3, SIZE - 2);
                g.fillRect(7, 1, 3, SIZE - 2);
            }
            case NEXT -> rightTriangle(g, 2, SIZE - 1);
            case LAST -> {
                rightTriangle(g, 0, SIZE - 3);
                g.fillRect(SIZE - 3, 1, 2, SIZE - 2);
            }
        }
        g.dispose();
    }

    private static void leftTriangle(Graphics2D g, int left, int right) {
        g.fillPolygon(new int[] { right, right, left }, new int[] { 1, SIZE - 1, SIZE / 2 }, 3);
    }

    private static void rightTriangle(Graphics2D g, int left, int right) {
        g.fillPolygon(new int[] { left, left, right }, new int[] { 1, SIZE - 1, SIZE / 2 }, 3);
    }
}

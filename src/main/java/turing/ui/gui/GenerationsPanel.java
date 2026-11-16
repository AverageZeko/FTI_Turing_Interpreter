package turing.ui.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JComponent;

/**
 * Draws the Rule 110 generations stored in the tape as a grid: one row per generation,
 * black cells for 1 and white cells for 0.
 */
class GenerationsPanel extends JComponent {
    private static final int CELL = 18;
    private static final int PADDING = 14;
    private static final int LABEL_WIDTH = 44;

    private final Font labelFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    private List<String> generations = List.of();

    void setGenerations(List<String> newGenerations) {
        boolean sizeChanged = !sameShape(newGenerations);
        this.generations = newGenerations;
        if (sizeChanged) {
            revalidate();
        }
        repaint();
    }

    private boolean sameShape(List<String> other) {
        return other.size() == generations.size() && widest(other) == widest(generations);
    }

    private static int widest(List<String> rows) {
        int max = 0;
        for (String row : rows) {
            max = Math.max(max, row.length());
        }
        return max;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PADDING * 2 + LABEL_WIDTH + widest(generations) * CELL,
                PADDING * 2 + Math.max(generations.size(), 1) * CELL);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        GuiColors colors = GuiColors.current();
        g.setFont(labelFont);
        FontMetrics metrics = g.getFontMetrics();

        if (generations.isEmpty()) {
            g.setColor(colors.mutedText);
            g.drawString("Todavia no hay generaciones completas en la cinta.", PADDING, PADDING + metrics.getAscent());
            g.dispose();
            return;
        }

        for (int row = 0; row < generations.size(); row++) {
            String cells = generations.get(row);
            int y = PADDING + row * CELL;

            g.setColor(colors.mutedText);
            g.drawString("G" + row, PADDING, y + (CELL + metrics.getAscent() - metrics.getDescent()) / 2);

            for (int column = 0; column < cells.length(); column++) {
                int x = PADDING + LABEL_WIDTH + column * CELL;
                g.setColor(cells.charAt(column) == '1' ? colors.one : colors.zero);
                g.fillRect(x, y, CELL, CELL);
                g.setColor(colors.grid);
                g.drawRect(x, y, CELL, CELL);
            }
        }
        g.dispose();
    }
}

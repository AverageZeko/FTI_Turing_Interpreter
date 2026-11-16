package turing.ui.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Draws the tape as a row of cells, highlighting the cell under the head.
 */
class TapePanel extends JComponent {
    private static final int CELL_WIDTH = 30;
    private static final int CELL_HEIGHT = 36;
    private static final int PADDING = 12;
    private static final int ARROW_AREA = 16;
    private static final int INDEX_AREA = 16;

    private final Font symbolFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private final Font indexFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    private String tape = "";
    private int headPosition = -1;

    /**
     * Shows a configuration and scrolls so the head stays visible.
     */
    void setConfiguration(String newTape, int newHeadPosition) {
        boolean sizeChanged = newTape.length() != tape.length();
        this.tape = newTape;
        this.headPosition = newHeadPosition;
        if (sizeChanged) {
            revalidate();
        }
        repaint();
        SwingUtilities.invokeLater(() -> scrollRectToVisible(headArea()));
    }

    void clear() {
        setConfiguration("", -1);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PADDING * 2 + tape.length() * CELL_WIDTH,
                PADDING * 2 + ARROW_AREA + CELL_HEIGHT + INDEX_AREA);
    }

    private Rectangle headArea() {
        int x = PADDING + Math.max(headPosition, 0) * CELL_WIDTH;
        // leave some cells of margin on both sides of the head
        return new Rectangle(x - CELL_WIDTH * 4, 0, CELL_WIDTH * 9, getHeight());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        GuiColors colors = GuiColors.current();

        int cellsTop = PADDING + ARROW_AREA;
        FontMetrics symbolMetrics = g.getFontMetrics(symbolFont);
        FontMetrics indexMetrics = g.getFontMetrics(indexFont);

        for (int i = 0; i < tape.length(); i++) {
            char symbol = tape.charAt(i);
            int x = PADDING + i * CELL_WIDTH;

            g.setColor(background(symbol, colors));
            g.fillRect(x, cellsTop, CELL_WIDTH, CELL_HEIGHT);
            g.setColor(colors.grid);
            g.drawRect(x, cellsTop, CELL_WIDTH, CELL_HEIGHT);

            g.setFont(symbolFont);
            g.setColor(symbol == '1' ? colors.oneText : colors.zeroText);
            String text = String.valueOf(symbol);
            g.drawString(text, x + (CELL_WIDTH - symbolMetrics.stringWidth(text)) / 2,
                    cellsTop + (CELL_HEIGHT + symbolMetrics.getAscent() - symbolMetrics.getDescent()) / 2);

            if (i % 5 == 0 || i == headPosition) {
                g.setFont(indexFont);
                g.setColor(colors.mutedText);
                String index = String.valueOf(i);
                g.drawString(index, x + (CELL_WIDTH - indexMetrics.stringWidth(index)) / 2,
                        cellsTop + CELL_HEIGHT + indexMetrics.getAscent() + 2);
            }
        }

        if (headPosition >= 0 && headPosition < tape.length()) {
            int x = PADDING + headPosition * CELL_WIDTH;
            g.setColor(colors.head);
            g.setStroke(new BasicStroke(3f));
            g.drawRect(x + 1, cellsTop + 1, CELL_WIDTH - 2, CELL_HEIGHT - 2);
            int center = x + CELL_WIDTH / 2;
            Polygon arrow = new Polygon(
                    new int[] { center - 7, center + 7, center },
                    new int[] { PADDING, PADDING, PADDING + ARROW_AREA - 3 }, 3);
            g.fillPolygon(arrow);
        }
        g.dispose();
    }

    private static Color background(char symbol, GuiColors colors) {
        return switch (symbol) {
            case '1' -> colors.one;
            case '0' -> colors.zero;
            case '#' -> colors.separator;
            case 'Δ' -> colors.blank;
            default -> colors.mark;
        };
    }
}

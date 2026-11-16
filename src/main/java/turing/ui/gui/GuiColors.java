package turing.ui.gui;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * Colors used to paint the tape and the generations, for light and dark themes.
 */
final class GuiColors {
    final Color one;
    final Color oneText;
    final Color zero;
    final Color zeroText;
    final Color separator;
    final Color blank;
    final Color mark;
    final Color grid;
    final Color head;
    final Color mutedText;

    private GuiColors(boolean dark) {
        if (dark) {
            one = new Color(0xE6E6E6);
            oneText = new Color(0x1E1E1E);
            zero = new Color(0x2F3236);
            zeroText = new Color(0xC8C8C8);
            separator = new Color(0x4A5A6E);
            blank = new Color(0x26282B);
            mark = new Color(0x8A6D1F);
            grid = new Color(0x55595E);
            head = new Color(0xFF8A3D);
            mutedText = new Color(0x9AA0A6);
        } else {
            one = new Color(0x202124);
            oneText = Color.WHITE;
            zero = Color.WHITE;
            zeroText = new Color(0x202124);
            separator = new Color(0xC9D6E8);
            blank = new Color(0xF3F4F6);
            mark = new Color(0xFFE08A);
            grid = new Color(0xB8BCC2);
            head = new Color(0xE8590C);
            mutedText = new Color(0x6B7280);
        }
    }

    /**
     * @return the palette that matches the current look and feel
     */
    static GuiColors current() {
        Color background = UIManager.getColor("Panel.background");
        boolean dark = background != null
                && (0.299 * background.getRed() + 0.587 * background.getGreen() + 0.114 * background.getBlue()) < 128;
        return new GuiColors(dark);
    }
}

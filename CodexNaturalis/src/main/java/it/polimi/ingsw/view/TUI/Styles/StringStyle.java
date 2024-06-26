package it.polimi.ingsw.view.TUI.Styles;

/**
 * This enum represents the styles that can be applied to a string.
 * Each style has a string representation that is the ESC code to apply it.
 */
public enum StringStyle {
    RESET("0"),
    RED_BG(248, 49, 47, true),
    GREEN_BG(0, 210, 106, true),
    YELLOW_BG("43"),
    BLUE_BG(0, 116, 186, true),
    PURPLE_BG(141, 101, 197, true),
    CYAN_BG("46"),
    WHITE_BG("47"),
    GREY_BG(168, 168, 168, true),
    GOLD_BG(255, 176, 46, true),
    LIGHT_BG(12, 8, 10, true),
    DARK_BG(64, 42, 50, true),
    BOLD("1"),
    ITALIC("3"),
    UNDERLINE("4"),
    RED_FOREGROUND(248, 49, 47, false),
    GREEN_FOREGROUND(0, 210, 106, false),
    YELLOW_FOREGROUND("33"),
    BLUE_FOREGROUND(0, 116, 186, false),
    PURPLE_FOREGROUND(141, 101, 197, false),
    GOLD_FOREGROUND(255, 176, 46, false),
    STRIKETHROUGH("9");

    /** The string representation of the style. */
    private final String style;

    /**
     * Creates a new StringStyle.
     * @param style The string representation of the style.
     */
    StringStyle(String style) {
        this.style = style;
    }

    /**
     * Creates a new StringStyle that represents a color. It can be a foreground or a background color.
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @param isBackground Whether the style is a background style.
     */
    StringStyle(int r, int g, int b, boolean isBackground) {
        this.style = (isBackground ? "4" : "3") + "8;2;" + r + ";" + g + ";" + b;
    }

    /**
     * Gets the string representation of the style.
     * @return The string representation of the style.
     */
    public String getStyle() {
        return style;
    }
}

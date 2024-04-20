package it.polimi.ingsw.view.TUI.Styles;

public enum StringStyle {

    RESET("0"),
    RED_BG(248, 49, 47),
    GREEN_BG(0, 210, 106),
    YELLOW_BG("43"),
    BLUE_BG(0, 116, 186),
    PURPLE_BG(141, 101, 197),
    CYAN_BG("46"),
    WHITE_BG("47"),
    GOLD_BG(255, 176, 46),
    LIGHT_BG(220, 213, 173),
    DARK_BG(64, 42, 50),
    BOLD("1"),
    ITALIC("3"),
    UNDERLINE("4"),
    STRIKETHROUGH("9");
    private final String style;

    StringStyle(String style) {
        this.style = style;
    }

    StringStyle(int r, int g, int b) {
        this.style = "48;2;" + r + ";" + g + ";" + b;
    }

    public String getStyle() {
        return style;
    }
}

package it.polimi.ingsw.view.TUI.Styles;

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
    STRIKETHROUGH("9");
    private final String style;

    StringStyle(String style) {
        this.style = style;
    }

    StringStyle(int r, int g, int b, boolean isBackground) {
        this.style = isBackground ? "4" : "3" + "8;2;" + r + ";" + g + ";" + b;
    }

    public String getStyle() {
        return style;
    }
}

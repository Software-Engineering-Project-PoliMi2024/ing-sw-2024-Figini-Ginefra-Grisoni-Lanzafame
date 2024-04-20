package it.polimi.ingsw.view.TUI.Styles;

import java.time.format.TextStyle;

public class DecoratedString {
    private final String content;
    private final StringStyle[] styles;

    public DecoratedString(String content, StringStyle[] styles) {
        this.content = content;
        this.styles = styles;
    }

    public DecoratedString(String content, StringStyle style) {
        this.content = content;
        this.styles = new StringStyle[]{style};
    }

    public String getContent() {
        return DecoratedString.buildStyleString(styles) + content + DecoratedString.buildStyleString(StringStyle.RESET);
    }

    private static String buildStyleString(StringStyle[] styles) {
        StringBuilder styleString = new StringBuilder();
        styleString.append("\u001B[");
        for (StringStyle style : styles) {
            styleString.append(style.getStyle()).append(";");
        }
        styleString.deleteCharAt(styleString.length() - 1);
        styleString.append("m");
        return styleString.toString();
    }

    private static String buildStyleString(StringStyle styles){
        return "\u001B[" + styles.getStyle() + "m";
    }

    public String toString() {
        return getContent();
    }
}

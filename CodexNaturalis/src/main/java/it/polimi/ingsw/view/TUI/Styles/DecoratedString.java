package it.polimi.ingsw.view.TUI.Styles;

/**
 * This class represents a string that has a style applied to it.
 * For the style ESC codes are used.

 */
public class DecoratedString {
    /** The content of the string. */
    private final String content;

    /** The styles applied to the string. */
    private final StringStyle[] styles;

    /**
     * Creates a new DecoratedString.
     * @param content The content of the string.
     * @param styles The styles applied to the string.
     */
    public DecoratedString(String content, StringStyle[] styles) {
        this.content = content;
        this.styles = styles;
    }

    /**
     * Creates a new DecoratedString.
     * @param content The content of the string.
     * @param style The style applied to the string.
     */
    public DecoratedString(String content, StringStyle style) {
        this.content = content;
        this.styles = new StringStyle[]{style};
    }

    /**
     * Gets the content of the string with the styles applied.
     * @return The content of the string with the styles applied.
     */
    public String getContent() {
        return DecoratedString.buildStyleString(styles) + content + DecoratedString.buildStyleString(StringStyle.RESET);
    }

    /**
     * Builds the style string that is the concatenation of all the styles as ESC codes.
     * @param styles The styles to apply.
     * @return The style string.
     */
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

    /**
     * Builds the style string that is the ESC code string of the given style.
     * @param style The styles to apply.
     * @return The style string.
     */
    private static String buildStyleString(StringStyle style){
        return "\u001B[" + style.getStyle() + "m";
    }

    /**
     * Gets the content as a string with the styles applied.
     * @return The content of the string with the styles applied.
     */
    @Override
    public String toString() {
        return getContent();
    }
}

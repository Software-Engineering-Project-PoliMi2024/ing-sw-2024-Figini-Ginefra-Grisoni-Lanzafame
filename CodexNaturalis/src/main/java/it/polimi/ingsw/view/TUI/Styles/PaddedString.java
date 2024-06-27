package it.polimi.ingsw.view.TUI.Styles;

/**
 * This class represents a string with a text alignment.
 * The string is padded to the specified width with spaces.
 */
public record PaddedString(String content, int width, TextAlign align) {
    /**
     * Returns the content with the specified alignment and padding.
     * @return The content with the specified alignment and padding.
     */
    public String toString() {
        switch (align) {
            case LEFT -> {
                return String.format("%-" + width + "s", content);
            }
            case CENTER -> {
                //Count lenght ignoring ANSI escape codes
                int contentLength = content.replaceAll("\u001B\\[[;\\d]*m", "").length();
                int offset = Math.max(0, (width - contentLength) / 2);
                int remainder = (width - contentLength) % 2;
                return String.format("%" + (offset + remainder + content.length()) + "s", content) + " ".repeat(offset);
            }
            case RIGHT -> {
                return String.format("%" + width + "s", content);
            }
            default -> {
                return content;
            }
        }
    }
}

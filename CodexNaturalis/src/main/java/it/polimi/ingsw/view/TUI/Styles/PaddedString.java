package it.polimi.ingsw.view.TUI.Styles;

public record PaddedString(String content, int width, TextAlign align) {
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

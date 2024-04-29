package it.polimi.ingsw.view.TUI.Styles;

public class PromptStyle {
    public static final String VerticalSeparator = "│";
    public static final String HorizontalSeparator = "─";
    public static final String HorizontalDoubleSeparator = "═";

    public static void printSeparator(int width){
        System.out.println(PromptStyle.HorizontalSeparator.repeat(width));
    }

    public static void printBetweenSeparators(String text, int width){
        System.out.print(PromptStyle.VerticalSeparator);
        System.out.print(new PaddedString(text, width, TextAlign.CENTER));
        System.out.println(PromptStyle.VerticalSeparator);
    }

    public static void printInABox(String text, int width){
        printSeparator(width+2);
        printBetweenSeparators(text, width);
        printSeparator(width+2);
    }
}

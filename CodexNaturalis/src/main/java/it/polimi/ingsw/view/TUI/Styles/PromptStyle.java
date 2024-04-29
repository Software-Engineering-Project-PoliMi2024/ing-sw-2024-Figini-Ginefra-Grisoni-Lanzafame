package it.polimi.ingsw.view.TUI.Styles;

import java.util.List;

public class PromptStyle {
    public static final String VerticalSeparator = "│";
    public static final String VerticalDoubleSeparator = "║";
    public static final String HorizontalSeparator = "─";
    public static final String HorizontalDoubleSeparator = "═";

    public static final String CornerTopLeft = "┌";
    public static final String CornerTopRight = "┐";
    public static final String CornerBottomLeft = "└";
    public static final String CornerBottomRight = "┘";

    public static final String CornerTopLeftDouble = "╔";
    public static final String CornerTopRightDouble = "╗";
    public static final String CornerBottomLeftDouble = "╚";
    public static final String CornerBottomRightDouble = "╝";

    public static final String CornerTopLeftRounded = "╭";
    public static final String CornerTopRightRounded = "╮";
    public static final String CornerBottomLeftRounded = "╰";
    public static final String CornerBottomRightRounded = "╯";


    public static final String ConnectionTop = "┬";
    public static final String ConnectionBottom = "┴";
    public static final String ConnectionLeft = "├";
    public static final String ConnectionRight = "┤";

    public static void printSeparator(int width, String separator, String leftCorner, String rightCorner){
        System.out.print(leftCorner);
        System.out.print(separator.repeat(width-2));
        System.out.println(rightCorner);
    }

    public static void printBetweenSeparators(String text, int width, String separator){
        System.out.print(separator);
        System.out.print(new PaddedString(text, width, TextAlign.CENTER));
        System.out.println(separator);
    }

    public static void printInABox(String text, int width){
        printSeparator(width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded);
        printBetweenSeparators(text, width, PromptStyle.VerticalSeparator);
        printSeparator(width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded);
    }

    public static void printInABoxDouble(String text, int width){
        printSeparator(width+2, PromptStyle.HorizontalDoubleSeparator, PromptStyle.CornerTopLeftDouble, PromptStyle.CornerTopRightDouble);
        printBetweenSeparators(text, width, PromptStyle.VerticalDoubleSeparator);
        printSeparator(width+2, PromptStyle.HorizontalDoubleSeparator, PromptStyle.CornerBottomLeftDouble, PromptStyle.CornerBottomRightDouble);
    }

    public static void printListInABox(String title, List<String> list, int width, int linesPerItem){
        printSeparator(width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded);
        printBetweenSeparators(title, width, PromptStyle.VerticalSeparator);
        printSeparator(width+2, PromptStyle.HorizontalSeparator, PromptStyle.ConnectionLeft, PromptStyle.ConnectionRight);

        int i = 0;
        for(String item : list){
            printBetweenSeparators(item, width, PromptStyle.VerticalSeparator);

            if(i % linesPerItem == linesPerItem-1 && i != list.size()-1){
                printBetweenSeparators("", width, PromptStyle.VerticalSeparator);
            }
            i++;
        }
        printSeparator(width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded);
    }

    public static final String Title = "\n" +
            " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ██╗███╗   ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗    \n" +
            "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ██║████╗  ██║    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝    \n" +
            "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██║██╔██╗ ██║    ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗    \n" +
            "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║██║╚██╗██║    ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║    \n" +
            "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║██║ ╚████║    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║    \n" +
            " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝╚═╝  ╚═══╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝    \n" +
            "                                                                                                                                         \n";
}

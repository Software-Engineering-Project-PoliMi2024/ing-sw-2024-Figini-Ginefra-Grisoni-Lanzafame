package it.polimi.ingsw.view.TUI.Styles;

import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;

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
        Printable printable = new Printable("");
        printable.print(leftCorner);
        printable.print(separator.repeat(width-2));
        printable.println(rightCorner);
        Printer.print(printable);
    }

    public static void printSeparator(Printable printable, int width, String separator, String leftCorner, String rightCorner){
        printable.print(leftCorner);
        printable.print(separator.repeat(width-2));
        printable.println(rightCorner);
    }

    public static void printSeparator(Printable printable, int width, String separator, String leftCorner, String rightCorner, StringStyle style){
        printable.print(new DecoratedString(leftCorner, style));
        printable.print(new DecoratedString(separator.repeat(width-2), style));
        printable.println(new DecoratedString(rightCorner, style));
    }

    public static void printBetweenSeparators(String text, int width, String separator){
        Printable printable = new Printable("");
        printable.print(separator);
        printable.print(new PaddedString(text, Math.max(width, text.length()), TextAlign.CENTER));
        printable.println(separator);
        Printer.print(printable);
    }

    public static void printBetweenSeparators(String text, int width, String separator, StringStyle style){
        Printable printable = new Printable("");
        printable.print(new DecoratedString(separator, style));
        printable.print(new DecoratedString(new PaddedString(text, Math.max(width, text.length()), TextAlign.CENTER).toString(), style));
        printable.println(new DecoratedString(separator, style));
        Printer.print(printable);
    }

    public static void printBetweenSeparators(Printable printable, String text, int width, String separator){
        printable.print(separator);
        printable.print(new PaddedString(text, Math.max(width, text.length()), TextAlign.CENTER));
        printable.println(separator);
    }

    public static void printBetweenSeparators(Printable printable, String text, int width, String separator, StringStyle style){
        printable.print(new DecoratedString(separator, style));
        printable.print(new DecoratedString(new PaddedString(text, Math.max(width, text.length()), TextAlign.CENTER).toString(), style));
        printable.println(new DecoratedString(separator, style));
    }

    public static void printInABox(String text, int width){
        Printable printable = new Printable("");
        printInABox(printable, text, width);
        Printer.print(printable);
    }

    public static void printInABox(Printable target, String text, int width){
        width = Math.max(width, text.length());
        printSeparator(target, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded);
        printBetweenSeparators(target, text, width, PromptStyle.VerticalSeparator);
        printSeparator(target, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded);
    }

    public static void printInABoxDouble(String text, int width){
        Printable printable = new Printable("");
        width = Math.max(width, text.length());
        printSeparator(printable, width+2, PromptStyle.HorizontalDoubleSeparator, PromptStyle.CornerTopLeftDouble, PromptStyle.CornerTopRightDouble);
        printBetweenSeparators(printable, text, width, PromptStyle.VerticalDoubleSeparator);
        printSeparator(printable, width+2, PromptStyle.HorizontalDoubleSeparator, PromptStyle.CornerBottomLeftDouble, PromptStyle.CornerBottomRightDouble);
        Printer.print(printable);
    }

    public static void printInABox(String text, int width, StringStyle style){
        Printable printable = new Printable("");
        printInABox(printable, text, width, style);
        Printer.print(printable);
    }

    public static void printInABox(Printable printable, String text, int width, StringStyle style){
        width = Math.max(width, text.length());
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded, style);
        printBetweenSeparators(printable, text, width, PromptStyle.VerticalSeparator, style);
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded, style);
    }

    public static void printListInABox(Printable printable, String title, List<String> list, int width, int linesPerItem){
        width = Math.max(width, title.length());
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded);
        printBetweenSeparators(printable, title, width, PromptStyle.VerticalSeparator);
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.ConnectionLeft, PromptStyle.ConnectionRight);

        int i = 0;
        for(String item : list){
            printBetweenSeparators(printable, item, width, PromptStyle.VerticalSeparator);

            if(i % linesPerItem == linesPerItem-1 && i != list.size()-1){
                printBetweenSeparators(printable, "", width, PromptStyle.VerticalSeparator);
            }
            i++;
        }
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded);
    }
    public static void printListInABox(String title, List<String> list, int width, int linesPerItem){
        Printable printable = new Printable("");
        printListInABox(printable, title, list, width, linesPerItem);
        Printer.print(printable);
    }

    public static final String Title = new DecoratedString("\n" +
            " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ██╗███╗   ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗    \n" +
            "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ██║████╗  ██║    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝    \n" +
            "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██║██╔██╗ ██║    ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗    \n" +
            "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║██║╚██╗██║    ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║    \n" +
            "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║██║ ╚████║    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║    \n" +
            " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝╚═╝  ╚═══╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝    \n" +
            "                                                                                                                                         \n",
            StringStyle.GREEN_FOREGROUND).toString();

    public static String GameStart = new DecoratedString("\n" +
            " ██████╗  █████╗ ███╗   ███╗███████╗    ███████╗████████╗ █████╗ ██████╗ ████████╗███████╗██████╗ \n" +
            "██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝██╔════╝██╔══██╗\n" +
            "██║  ███╗███████║██╔████╔██║█████╗      ███████╗   ██║   ███████║██████╔╝   ██║   █████╗  ██║  ██║\n" +
            "██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ╚════██║   ██║   ██╔══██║██╔══██╗   ██║   ██╔══╝  ██║  ██║\n" +
            "╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ███████║   ██║   ██║  ██║██║  ██║   ██║   ███████╗██████╔╝\n" +
            " ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝    ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═════╝ \n" +
            "                                                                                                  \n",
            StringStyle.GOLD_FOREGROUND).toString();
}

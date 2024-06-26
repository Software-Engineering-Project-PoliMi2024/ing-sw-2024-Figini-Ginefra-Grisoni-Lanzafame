package it.polimi.ingsw.view.TUI.Styles;

import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;

import java.util.List;

/**
 * This class contains the styles used for nicer printing in TUI.
 * All the methods and fields are static.
 * It contains method to print separators, boxes and lists in boxes.
 */
public class PromptStyle {
    public static final String VerticalSeparator = "│";
    public static final String VerticalDoubleSeparator = "║";
    public static final String HorizontalSeparator = "─";
    public static final String HorizontalDoubleSeparator = "═";
    public static final String CornerTopLeftDouble = "╔";
    public static final String CornerTopRightDouble = "╗";
    public static final String CornerBottomLeftDouble = "╚";
    public static final String CornerBottomRightDouble = "╝";
    public static final String CornerTopLeftRounded = "╭";
    public static final String CornerTopRightRounded = "╮";
    public static final String CornerBottomLeftRounded = "╰";
    public static final String CornerBottomRightRounded = "╯";
    public static final String ConnectionLeft = "├";
    public static final String ConnectionRight = "┤";

    /**
     * Prints to the given printable a separator with the given width.
     * @param printable The printable to print to.
     * @param width The width of the separator.in #chars.
     * @param separator The string to repeat to make the separator.
     * @param leftCorner The string to print at the left corner.
     * @param rightCorner The string to print at the right corner.
     */
    public static void printSeparator(Printable printable, int width, String separator, String leftCorner, String rightCorner){
        printable.print(leftCorner);
        printable.print(separator.repeat(width-2));
        printable.println(rightCorner);
    }

    /**
     * Prints to the given printable a separator with the given width and style.
     * @param printable The printable to print to.
     * @param width The width of the separator.in #chars.
     * @param separator The string to repeat to make the separator.
     * @param leftCorner The string to print at the left corner.
     * @param rightCorner The string to print at the right corner.
     * @param style The style to apply to the separator.
     */
    public static void printSeparator(Printable printable, int width, String separator, String leftCorner, String rightCorner, StringStyle style){
        printable.print(new DecoratedString(leftCorner, style));
        printable.print(new DecoratedString(separator.repeat(width-2), style));
        printable.println(new DecoratedString(rightCorner, style));
    }

    /**
     * Prints to the given printable the given text between the given separator, padded to the given width-
     * @param printable The printable to print to.
     * @param width The width of the separator.in #chars.
     * @param separator The string to repeat to make the separator.
     */
    public static void printBetweenSeparators(Printable printable, String text, int width, String separator){
        printable.print(separator);
        printable.print(new PaddedString(text, Math.max(width, text.length()), TextAlign.CENTER));
        printable.println(separator);
    }

    /**
     * Prints to the given printable the given text between the given separator, padded to the given width and with the given style.
     * @param printable The printable to print to.
     * @param width The width of the separator.in #chars.
     * @param separator The string to repeat to make the separator.
     * @param style The style to apply to the text.
     */
    public static void printBetweenSeparators(Printable printable, String text, int width, String separator, StringStyle style){
        printable.print(new DecoratedString(separator, style));
        printable.print(new DecoratedString(new PaddedString(text, Math.max(width, text.length()), TextAlign.CENTER).toString(), style));
        printable.println(new DecoratedString(separator, style));
    }

    /**
     * Prints to the console the given text encapsulated in a box with the given width.
     * @param text The text to print.
     * @param width The width of the box.
     */
    public static void printInABox(String text, int width){
        Printable printable = new Printable("");
        printInABox(printable, text, width);
        Printer.print(printable);
    }

    /**
     * Prints to the given printable the given text encapsulated in a box with the given width.
     * @param target The printable to print to.
     * @param text The text to print.
     * @param width The width of the box.
     */
    public static void printInABox(Printable target, String text, int width){
        width = Math.max(width, text.length());
        printSeparator(target, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded);
        printBetweenSeparators(target, text, width, PromptStyle.VerticalSeparator);
        printSeparator(target, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded);
    }

    /**
     * Prints to the console the given text encapsulated in a box with double edges with the given width.
     * @param text The text to print.
     * @param width The width of the box.
     */
    public static void printInABoxDouble(String text, int width){
        Printable printable = new Printable("");
        width = Math.max(width, text.length());
        printSeparator(printable, width+2, PromptStyle.HorizontalDoubleSeparator, PromptStyle.CornerTopLeftDouble, PromptStyle.CornerTopRightDouble);
        printBetweenSeparators(printable, text, width, PromptStyle.VerticalDoubleSeparator);
        printSeparator(printable, width+2, PromptStyle.HorizontalDoubleSeparator, PromptStyle.CornerBottomLeftDouble, PromptStyle.CornerBottomRightDouble);
        Printer.print(printable);
    }

    /**
     * Prints to the console the given text encapsulated in a box with the given width and style.
     * @param text The text to print.
     * @param width The width of the box.
     * @param style The style to apply to the text.
     */
    public static void printInABox(String text, int width, StringStyle style){
        Printable printable = new Printable("");
        printInABox(printable, text, width, style);
        Printer.print(printable);
    }

    /**
     * Prints to the given printable the given text encapsulated in a box with the given width and style.
     * @param printable The printable to print to.
     * @param text The text to print.
     * @param width The width of the box.
     * @param style The style to apply to the text.
     */
    public static void printInABox(Printable printable, String text, int width, StringStyle style){
        width = Math.max(width, text.length());
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerTopLeftRounded, PromptStyle.CornerTopRightRounded, style);
        printBetweenSeparators(printable, text, width, PromptStyle.VerticalSeparator, style);
        printSeparator(printable, width+2, PromptStyle.HorizontalSeparator, PromptStyle.CornerBottomLeftRounded, PromptStyle.CornerBottomRightRounded, style);
    }

    /**
     * Prints to the given printable the given list of strings encapsulated in a box with the given width.
     * A vertical separator is printed every linesPerItem lines.
     * @param printable The printable to print to.
     * @param title The title of the box.
     * @param list The list of strings to print.
     * @param width The width of the box.
     * @param linesPerItem The number of lines to print between separators.
     */
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

    /**
     * Prints to the console the given list of strings encapsulated in a box with the given width.
     * A vertical separator is printed every linesPerItem lines.
     * @param title The title of the box.
     * @param list The list of strings to print.
     * @param width The width of the box.
     * @param linesPerItem The number of lines to print between separators.
     */
    public static void printListInABox(String title, List<String> list, int width, int linesPerItem){
        Printable printable = new Printable("");
        printListInABox(printable, title, list, width, linesPerItem);
        Printer.print(printable);
    }

    /** The ASCII art for the title of the game. */
    public static final String Title = new DecoratedString("\n" +
            " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ██╗███╗   ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗    \n" +
            "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ██║████╗  ██║    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝    \n" +
            "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██║██╔██╗ ██║    ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗    \n" +
            "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║██║╚██╗██║    ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║    \n" +
            "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║██║ ╚████║    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║    \n" +
            " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝╚═╝  ╚═══╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝    \n" +
            "                                                                                                                                         \n",
            StringStyle.GREEN_FOREGROUND).toString();

    /** The ASCII art for the start of the game. */
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

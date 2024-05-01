package it.polimi.ingsw.view.TUI.Printing;

import java.util.Queue;

/**
 * This class is used to print strings to the console in a mutually exclusive fashion.
 */
public class Printer {
    /**
     * This method prints the content of a Printable object to the console.
     * @param printable the Printable object to be printed.
     */
    public static synchronized void print(Printable printable){
        printable.flush();
    }

    /**
     * This method prints a string to the console.
     * @param content the string to be printed.
     */
    public static synchronized void print(String content){
        Printable printable = new Printable(content);
        printable.flush();
    }

    /**
     * This method prints a string to the console followed by a newline character.
     * @param content the string to be printed.
     */
    public static synchronized void println(String content){
        Printable printable = new Printable("");
        printable.println(content);
        printable.flush();
    }

    /**
     * This method prints a string to the console followed by a newline character and a tab character.
     * @param content the string to be printed.
     */
    public static synchronized void printlnt(String content){
        Printable printable = new Printable("");
        printable.printlnt(content);
        printable.flush();
    }
}
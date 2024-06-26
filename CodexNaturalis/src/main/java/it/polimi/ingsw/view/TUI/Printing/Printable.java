package it.polimi.ingsw.view.TUI.Printing;

import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PaddedString;

/**
 * This class is a container for strings that are atomically printed to the console.
 * That is, the content of the Printable object is printed all at once and cannot be interrupted by other print statements.
 */
public class Printable {
    /** The content of the Printable object. */
    private final StringBuilder content;

    /**
     * This constructor creates a new Printable object with the given content.
     * @param content the content of the Printable object.
     */
    public Printable(String content){
        this.content = new StringBuilder();
        this.print(content);
    }

    /**
     * This method appends a string to the content of the Printable object.
     * @param content the string to be appended.
     */
    public void print(String content){
        this.content.append(content);
    }

    /**
     * This method appends a DecoratedString to the content of the Printable object.
     * @param content the DecoratedString to be appended.
     */
    public void print(DecoratedString content){
        this.content.append(content.toString());
    }

    /**
     * This method appends a PaddedString to the content of the Printable object.
     * @param content the PaddedString to be appended.
     */
    public void print(PaddedString content){
        this.content.append(content.toString());
    }

    /**
     * This method appends the content of another Printable object to the content of the Printable object.
     * @param other the Printable object whose content is to be appended.
     */
    public void print(Printable other){
        this.content.append(other.content);
    }

    /**
     * This method appends a string to the content of the Printable object followed by a newline character.
     * @param content the string to be appended.
     */
    public void println(String content){
        this.content.append(content).append("\n");
    }

    /**
     * This method appends a DecoratedString to the content of the Printable object followed by a newline character.
     * @param content the DecoratedString to be appended.
     */
    public void println(DecoratedString content){
        this.content.append(content).append("\n");
    }

    /**
     * This method appends a PaddedString to the content of the Printable object followed by a newline character.
     * @param content the PaddedString to be appended.
     */
    public void println(PaddedString content){
        this.content.append(content).append("\n");
    }

    /**
     * This method appends the content of another Printable object to the content of the Printable object followed by a newline character.
     * @param other the Printable object whose content is to be appended.
     */
    public void println(Printable other){
        this.content.append(other.content).append("\n");
    }

    /**
     * This method appends a string to the content of the Printable object followed by a newline character and a tab character.
     * @param content the string to be appended.
     */
    public void printlnt(String content){
        this.content.append(content).append("\n\t");
    }

    /**
     * This method prints the content of the Printable object to the console.
     */
    public void flush(){
        System.out.print(content);
    }

    /**
     * This method returns the height of the Printable object. That is, the number of lines in the content of the Printable object.
     * @return the height of the Printable object.
     */
    public int getHeight(){
        return content.toString().split("\n").length;
    }

    /**
     * The i-th line of the Printable object.
     * @param i the number of the line to get.
     * @return the content of the Printable object.
     */
    public String getRow(int i){
        return content.toString().split("\n")[i];
    }
}

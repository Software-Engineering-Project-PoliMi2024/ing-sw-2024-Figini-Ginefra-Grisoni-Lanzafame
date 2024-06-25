package it.polimi.ingsw.view.TUI.Printing;

import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PaddedString;

/**
 * This class is used to store strings and print them to the console.
 */
public class Printable {
    private final StringBuilder content;

    /**
     * This constructor creates a new Printable object with the given content.
     * @param content the content of the Printable object.
     */
    public Printable(String content){
        this.content = new StringBuilder();
        this.print(content);
    }

    public Printable(String character, int times){
        this.content = new StringBuilder();
        this.content.append(String.valueOf(character).repeat(Math.max(0, times)));
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

    public void println(DecoratedString content){
        this.content.append(content).append("\n");
    }

    public void println(PaddedString content){
        this.content.append(content).append("\n");
    }

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

    public int getHeight(){
        return content.toString().split("\n").length;
    }

    public String getRow(int height){
        return content.toString().split("\n")[height];
    }
}

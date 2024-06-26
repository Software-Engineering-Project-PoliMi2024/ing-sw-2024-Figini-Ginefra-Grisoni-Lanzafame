package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a drawable object. That being an object that can be drawn as an array of strings.
 */
public class Drawable implements Serializable {
    /** The width of the drawable. */
    private int width;
    /** The height of the drawable. */
    private int height;
    /** The content of the drawable as 2d matrix of Strings. */
    private String[][] content;

    /**
     * Creates a new Drawable.
     * @param width The width of the drawable.
     * @param height The height of the drawable.
     */
    public Drawable(int width, int height){
        this.width = width;
        this.height = height;
        this.content = new String[height][width];
    }

    /**
     * Fills the content of the drawable with the given filler.
     * @param filler The filler to fill the content with.
     */
    public void fillContent(String filler){
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                this.content[i][j] = filler;
            }
        }
    }

    /**
     * Adds the given content to the drawable at the given coordinates.
     * @param content The content to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @throws IndexOutOfBoundsException if the coordinates are out of the drawable.
     */
    public void addContent(String content, int x, int y){
        if(x >= 0 && x < this.width && y >= 0 && y < this.height)
            this.content[y][x] = content;
        else
            throw new IndexOutOfBoundsException("The coordinates are out of the drawable. Got: " + x + ", " + y);
    }

    /**
     * @return The content of the drawable.
     */
    public String[][] getContent(){
        return this.content;
    }

    /**
     * @return The width of the drawable.
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * @return The height of the drawable.
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * Sets the width of the drawable. If the new width is smaller than the current one, the content will be cropped. If it is bigger, the content will be padded with null.
     * @param width The new width.
     */
    public void setWidth(int width){
        String[][] newContent = new String[this.height][width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < width; j++) {
                if(j < this.width)
                    newContent[i][j] = this.content[i][j];
            }
        }
        this.content = newContent;
        this.width = width;
    }

    /**
     * Sets the height of the drawable. If the new height is smaller than the current one, the content will be cropped. If it is bigger, the content will be padded with null.
     * @param height The new height.
     */
    public void setHeight(int height){
        String[][] newContent = new String[height][this.width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < this.width; j++) {
                if(i < this.height)
                    newContent[i][j] = this.content[i][j];
            }
        }
        this.content = newContent;
        this.height = height;
    }

    /**
     * Sets the content of the drawable.
     * @param content The new content.
     */
    public void setContent(String[][] content){
        if(content.length != this.height || content[0].length != this.width)
            throw new IllegalArgumentException("The content must have the same dimensions as the drawable");

        for(int i = 0; i < this.height; i++){
            System.arraycopy(content[i], 0, this.content[i], 0, this.width);
        }
    }

    /**
     * Converts the drawable to a string ready to be printed.
     * @return The string representation of the drawable.
     */
    public String toString(){
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                str.append(this.content[i][j]);
            }
            str.append('\n');
        }

        return str.toString();
    }
}

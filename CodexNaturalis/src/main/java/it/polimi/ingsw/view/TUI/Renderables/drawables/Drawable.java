package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drawable implements Serializable {
    private int width;
    private int height;
    private final String[][] content;


    public Drawable(int width, int height){
        this.width = width;
        this.height = height;
        this.content = new String[height][width];
    }

    public void clearContent(){
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                this.content[i][j] = " ";
            }
        }
    }

    public void fillContent(String filler){
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                this.content[i][j] = filler;
            }
        }
    }

    public void addContent(String content, int x, int y){
        if(x >= 0 && x < this.width && y >= 0 && y < this.height)
            this.content[y][x] = content;
        else
            throw new IndexOutOfBoundsException("The coordinates are out of the drawable. Got: " + x + ", " + y);
    }

    public String[][] getContent(){
        return this.content;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public String getCharAt(int x, int y){
        return this.content[y][x];
    }

    public void setContent(String[][] content){
        if(content.length != this.height || content[0].length != this.width)
            throw new IllegalArgumentException("The content must have the same dimensions as the drawable");

        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                this.content[i][j] = content[i][j];
            }
        }
    }

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

package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Drawable extends Renderable {
    private Position position;
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
        this.content[y][x] = content;
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

    public void render(){
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                System.out.print(this.content[i][j]);
            }
            System.out.println();
        }
    }
}

package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;

import java.util.ArrayList;
import java.util.List;

public abstract class Drawable extends Renderable {
    private Position position;
    private int width;
    private int height;

    private final List<String> content = new ArrayList<>();


    public Drawable(int width, int height){

        this.width = width;
        this.height = height;
    }

    public void clearContent(){
        this.content.clear();
    }

    public void addContent(String content){
        this.content.add(content);
    }

    public List<String> getContent(){
        return this.content;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public char getCharAt(int x, int y){
        return this.content.get(y).charAt(x);
    }
}

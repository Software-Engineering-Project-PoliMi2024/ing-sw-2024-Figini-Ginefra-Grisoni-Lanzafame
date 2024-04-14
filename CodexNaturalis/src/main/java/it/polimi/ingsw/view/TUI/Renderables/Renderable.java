package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.view.TUI.observers.InputObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Renderable implements InputObserver {
    private boolean active = false;

    private int width;
    private int height;

    private final List<String> content = new ArrayList<>();

    public Renderable(int width, int height){
        this.width = width;
        this.height = height;
    }

    public abstract void render();

    public abstract void update();

    public abstract void updateInput(String input);

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return this.active;
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

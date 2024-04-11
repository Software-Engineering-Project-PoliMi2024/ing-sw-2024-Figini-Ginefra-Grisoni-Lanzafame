package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;

public abstract class View {
    private final Controller controller;

    public View(Controller controller){
        this.controller = controller;
    }

    public void run(){}

    public Controller getController(){
        return this.controller;
    }

}

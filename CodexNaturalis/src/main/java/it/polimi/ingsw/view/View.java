package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.ViewState;

public abstract class View {
    private final Controller controller;

    private ViewState currentState = null;

    public View(Controller controller){

        this.controller = controller;
    }

    public void run(){}

    public Controller getController(){
        return this.controller;
    }

    public ViewState getCurrentState(){
        return this.currentState;
    }

    public void setState(ViewState state){
        this.currentState = state;
    }

    public void transitionTo(ViewState state){
        this.currentState = state;
    }

}

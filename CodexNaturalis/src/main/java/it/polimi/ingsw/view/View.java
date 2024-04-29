package it.polimi.ingsw.view;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.ViewInterface;

public abstract class View implements ViewInterface {
    private final ControllerInterface controller;

    private ViewState currentState = null;

    public View(ControllerInterface controller){

        this.controller = controller;
    }

    public void run(){}

    public ControllerInterface getController(){
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

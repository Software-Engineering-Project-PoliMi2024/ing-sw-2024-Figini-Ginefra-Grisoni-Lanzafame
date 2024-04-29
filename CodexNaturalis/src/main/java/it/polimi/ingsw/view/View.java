package it.polimi.ingsw.view;

import it.polimi.ingsw.controller2.ConnectionLayerClient;
import it.polimi.ingsw.controller2.ViewInterface;

public abstract class View implements ViewInterface {
    private final ConnectionLayerClient controller;

    private ViewState currentState = null;

    public View(ConnectionLayerClient controller){

        this.controller = controller;
    }

    public void run(){}

    public ConnectionLayerClient getController(){
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

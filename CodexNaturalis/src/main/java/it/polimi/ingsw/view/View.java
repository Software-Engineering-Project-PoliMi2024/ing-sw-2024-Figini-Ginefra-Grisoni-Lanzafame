package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller2.ViewInterface;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;

import java.util.List;

public abstract class View implements ViewInterface {
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

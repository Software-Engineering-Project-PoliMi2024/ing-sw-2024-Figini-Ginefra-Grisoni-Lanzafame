package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.lightModel.LightLobby;
import it.polimi.ingsw.model.lightModel.diffs.ModelDiff;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.ViewState;

import java.util.List;

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

    public abstract void log(String logMsg);

    public abstract void updateGameList(ModelDiff<LightLobby> diff);

    public abstract void setLobbyGame(Game game);

    public abstract void setStartCard(StartCard card);

    public abstract void updateResourceDeck(Deck<ResourceCard> deck);

    public abstract void updateGoldDeck(Deck<GoldCard> deck);

    public abstract void setSecretObjectiveOptions(List<ObjectiveCard> objectiveCards);

    public abstract void leaderBoardSetNicks(String[] nicks);

    public abstract void leaderBoardUpdatePoints(int[] points);

    public abstract void playerOrderUpdateActivesNicks(String[] nicks);

    public abstract void playerOrderSetPlayerOrder(String[] nicks);

    public abstract void playerOrderUpdateCurrentPlayer(String nick);

    public abstract void updateCodex(Codex codex);

    public abstract void updateHand(Hand hand);

    public abstract void setFinalRanking(String[] nicks, int[] points);
}

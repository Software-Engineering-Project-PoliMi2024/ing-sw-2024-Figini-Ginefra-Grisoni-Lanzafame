package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.view.ViewState;

import java.util.List;

public interface ViewInterface {


    void setState(ViewState state);
    void transitionTo(ViewState state);
    void log(String logMsg);
    void updateGameList(ModelDiffs<LightLobby> diff);
    void updateLobby(ModelDiffs<LightLobby> diff);
    void setStartCard(LightCard startCard);
    void updateDecks(ModelDiffs<LightDeck> deck);
    void setSecretObjectiveOptions(List<LightCard> objectiveCards);
    void leaderBoardSetNicks(String[] nicks);
    void leaderBoardUpdatePoints(int[] points);
    void playerOrderUpdateActivesNicks(String[] nicks);
    void playerOrderSetPlayerOrder(String[] nicks);
    void playerOrderUpdateCurrentPlayer(String nick);
    void updateCodex(ModelDiffs<LightCodex> codex);
    void updateHand(ModelDiffs<LightHand> hand);
    void setFinalRanking(String[] nicks, int[] points);
}

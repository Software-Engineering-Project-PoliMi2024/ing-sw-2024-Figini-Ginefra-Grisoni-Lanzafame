package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.view.ViewState;

import java.util.List;

public interface ViewInterface extends DiffSubscriber {
    void setState(ViewState state);
    void transitionTo(ViewState state);
    void log(String logMsg);
    void updateLobbyList(ModelDiffs<LightLobbyList> diff);
    void updateLobby(ModelDiffs<LightLobby> diff);
    void updateGame(ModelDiffs<LightGame> diff);
    void setFinalRanking(String[] nicks, int[] points);
}

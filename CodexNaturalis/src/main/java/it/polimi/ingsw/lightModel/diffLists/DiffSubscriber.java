package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.controller2.ViewInterface;
import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.util.Map;

public interface DiffSubscriber extends Serializable {
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff);
    public void updateLobby(ModelDiffs<LightLobby> diff);
    public void updateGame(ModelDiffs<LightGame> diff);
}

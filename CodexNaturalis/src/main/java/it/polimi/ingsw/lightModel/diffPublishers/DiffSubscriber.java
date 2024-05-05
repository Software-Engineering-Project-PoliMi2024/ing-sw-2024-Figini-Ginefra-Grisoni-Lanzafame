package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface DiffSubscriber extends Serializable {
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff);
    public void updateLobby(ModelDiffs<LightLobby> diff);
    public void updateGame(ModelDiffs<LightGame> diff);
}

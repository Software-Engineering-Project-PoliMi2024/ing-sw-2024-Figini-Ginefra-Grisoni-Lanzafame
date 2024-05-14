package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

public class UpdateLobbyMsg extends ServerMsg{
    private final ModelDiffs<LightLobby> diff;

    public UpdateLobbyMsg(ModelDiffs<LightLobby> diff) {
        this.diff = diff;
    }

    public ModelDiffs<LightLobby> getDiff() {
        return diff;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().updateLobby(diff);
    }
}

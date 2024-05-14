package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

public class UpdateLobbyListMsg extends ServerMsg{
    private final ModelDiffs<LightLobbyList> diff;

    public UpdateLobbyListMsg(ModelDiffs<LightLobbyList> diff) {
        this.diff = diff;
    }

    public ModelDiffs<LightLobbyList> getDiff() {
        return diff;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().updateLobbyList(diff);
    }
}

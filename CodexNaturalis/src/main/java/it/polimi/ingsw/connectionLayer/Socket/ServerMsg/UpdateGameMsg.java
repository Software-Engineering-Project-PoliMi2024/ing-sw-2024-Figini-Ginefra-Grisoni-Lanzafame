package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

public class UpdateGameMsg extends ServerMsg{
    private final ModelDiffs<LightGame> diff;

    public UpdateGameMsg(ModelDiffs<LightGame> diff) {
        this.diff = diff;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().updateGame(diff);
    }
}

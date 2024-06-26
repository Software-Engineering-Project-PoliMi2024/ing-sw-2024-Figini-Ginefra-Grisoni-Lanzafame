package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

/**
 * Update game message class
 */
public class UpdateGameMsg extends ServerMsg{
    /** Game diffs */
    private final ModelDiffs<LightGame> diff;

    /**
     * Class constructor
     * @param diff Game diffs
     */
    public UpdateGameMsg(ModelDiffs<LightGame> diff) {
        this.diff = diff;
    }

    /**
     * Call the updateGame method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().updateGame(diff);
    }
}

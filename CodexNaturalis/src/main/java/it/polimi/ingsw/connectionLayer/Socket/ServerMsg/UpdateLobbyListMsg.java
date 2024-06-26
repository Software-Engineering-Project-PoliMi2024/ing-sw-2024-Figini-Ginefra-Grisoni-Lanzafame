package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

/**
 * Update lobby list message class
 */
public class UpdateLobbyListMsg extends ServerMsg{
    /** Lobby list diffs */
    private final ModelDiffs<LightLobbyList> diff;
    /**
     * Class constructor
     * @param diff Lobby list diffs
     */
    public UpdateLobbyListMsg(ModelDiffs<LightLobbyList> diff) {
        this.diff = diff;
    }

    /**
     * Call the updateLobbyList method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().updateLobbyList(diff);
    }
}

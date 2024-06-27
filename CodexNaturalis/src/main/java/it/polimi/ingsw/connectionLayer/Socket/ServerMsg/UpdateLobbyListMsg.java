package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

/**
 * Update lobby list message class
 */
public class UpdateLobbyListMsg extends ServerMsg{
    /** a Lobby list diffs containing the changes to apply to the lightLobbyList to display new or removed lobbies*/
    private final ModelDiffs<LightLobbyList> diff;
    /**
     * Class constructor
     * @param diff a Lobby list diffs containing the changes to apply to the lightLobbyList to display new or removed lobbies
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

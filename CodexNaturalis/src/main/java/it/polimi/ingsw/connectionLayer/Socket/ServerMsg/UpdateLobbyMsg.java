package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

/**
 * Update lobby message class
 */
public class UpdateLobbyMsg extends ServerMsg{
    /** a Lobby diffs containing the changes to apply to the lightLobby such as new players or removed players */
    private final ModelDiffs<LightLobby> diff;

    /**
     * Class constructor
     * @param diff a Lobby diffs containing the changes to apply to the lightLobby such as new players or removed players
     */
    public UpdateLobbyMsg(ModelDiffs<LightLobby> diff) {
        this.diff = diff;
    }

    /**
     * Call the updateLobby method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().updateLobby(diff);
    }
}

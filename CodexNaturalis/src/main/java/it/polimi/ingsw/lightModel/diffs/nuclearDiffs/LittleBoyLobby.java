package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

/**
 * This class reset the lightLobby removing every diff that was applied to it
 */
public class LittleBoyLobby extends LobbyDiff {

    /**
     * Apply the update to the LightLobby
     * @param lightLobby the LightLobby to which the diff applies
     */
    @Override
    public void apply(LightLobby lightLobby) {
        lightLobby.reset();
    }
}

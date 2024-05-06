package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

public class LittleBoyLobby extends LobbyDiff {
    @Override
    public void apply(LightLobby lightLobby) {
        lightLobby.reset();
    }
}

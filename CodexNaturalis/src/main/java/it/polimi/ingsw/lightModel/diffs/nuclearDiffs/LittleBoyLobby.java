package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.LobbyDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;

public class LittleBoyLobby extends LobbyDiff {
    @Override
    public void apply(LightLobby lightLobby) {
        lightLobby.reset();
    }
}

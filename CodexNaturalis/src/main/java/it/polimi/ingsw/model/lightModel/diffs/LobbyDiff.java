package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.lightModel.LightLobby;

import java.util.List;

public record LobbyDiff(List<String> remove, List<String> add) implements ModelDiffs<LightLobby>{
    public void apply(LightLobby lightLobby) {
        lightLobby.nickDiff(remove, add);
    }
}
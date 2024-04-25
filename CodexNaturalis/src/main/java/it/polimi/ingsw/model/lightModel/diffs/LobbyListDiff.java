package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.lightModel.LightLobby;
import it.polimi.ingsw.model.lightModel.LightLobbyList;

import java.util.List;

public record LobbyListDiff(List<LightLobby> removedGame,
                            List<LightLobby> addedGame) implements ModelDiffs<LightLobbyList> {
    @Override
    public void apply(LightLobbyList lightLobbyList) {
        List<LightLobby> tmp = lightLobbyList.getLobbies();
        tmp.forEach(removedGame::remove);
        tmp.addAll(this.addedGame);
        lightLobbyList.setLobbies(tmp);
    }
}

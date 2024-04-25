package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.ModelDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.List;

public class LightGameList implements ModelDifferentiable<LightLobby> {
    private final List<LightLobby> lobbies;

    public LightGameList(List<LightLobby> lobbies) {
        this.lobbies = lobbies;
    }

    public List<LightLobby> getLobbies() {
        return lobbies;
    }

    @Override
    public void applyDiff(ModelDiff<LightLobby> diff) {
        diff.removeList().forEach(lobbies::remove);
        lobbies.addAll(diff.addList());
    }
}

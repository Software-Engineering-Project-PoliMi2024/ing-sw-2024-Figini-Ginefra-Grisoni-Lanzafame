package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.ModelDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.List;

public class LightLobby implements ModelDifferentiable<String> {
    private final List<String> nicknames;
    private final String name;

    public LightLobby(List<String> nicknames, String name) {
        this.nicknames = nicknames;
        this.name = name;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public String getName() {
        return name;
    }

    @Override
    public void applyDiff(ModelDiff<String> diff) {
        nicknames.removeAll(diff.removeList());
        nicknames.addAll(diff.addList());
    }
}

package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.LobbyDiff;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;

import java.util.List;

public interface DiffSubscriber {
    void update(ModelDiffs<Differentiable> diffs);
    String getNickname();
}

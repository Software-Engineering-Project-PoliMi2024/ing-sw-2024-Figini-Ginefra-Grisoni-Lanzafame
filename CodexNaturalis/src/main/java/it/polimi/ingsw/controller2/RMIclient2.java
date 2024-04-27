package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;

import java.util.List;

public class RMIclient2 implements DiffSubscriber {
    @Override
    public void update(ModelDiffs<Differentiable> diffs) {

    }

    @Override
    public String getNickname() {
        return null;
    }

}

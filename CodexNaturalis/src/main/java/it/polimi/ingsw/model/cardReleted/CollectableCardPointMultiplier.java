package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.util.Collection;
import java.util.Map;

public class CollectableCardPointMultiplier implements ObjectiveCardPointMultiplier{
    final private Map<Collectable, Integer> targets;

    public CollectableCardPointMultiplier(){
        targets = null;
    }

    @Override
    public int getMultiplier(Codex codex) {
        return 0;
    }
}

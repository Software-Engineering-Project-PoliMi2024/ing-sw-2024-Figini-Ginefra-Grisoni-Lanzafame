package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectableCardPointMultiplier implements ObjectiveCardPointMultiplier{
    final private HashMap<Collectable, Integer> targets;

    /** @param targets HashMap of the collectables that the player has to collect to get the multiplier*/
    public CollectableCardPointMultiplier(HashMap <Collectable, Integer> targets){
        this.targets = targets;
    }

    /** @param codex the codex where the card is inserted
     *  @return the multiplicator of the points*/
    @Override
    public int getMultiplier(Codex codex) {
        ArrayList<Integer> multipliersTargetCollectables = new ArrayList<>();
        for (Collectable c : targets.keySet()){
            if(targets.get(c) > 0){
                multipliersTargetCollectables
                        .add(codex.getEarnedCollectables()
                                .get(c) / targets.get(c));
            }
        }

        return multipliersTargetCollectables.stream().mapToInt(i -> i).min().orElse(0);
    }
}

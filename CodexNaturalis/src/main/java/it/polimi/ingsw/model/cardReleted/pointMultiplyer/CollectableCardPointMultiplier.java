package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.playerReleted.Codex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectableCardPointMultiplier implements ObjectiveCardPointMultiplier {
    final private Map<Collectable, Integer> targets;

    /** @param targets HashMap of the collectables that the player has to collect to get the multiplier*/
    public CollectableCardPointMultiplier(Map<Collectable, Integer> targets){
        this.targets = targets;
    }

    /** @param other the multiplier to copy*/
    public CollectableCardPointMultiplier(CollectableCardPointMultiplier other){
        this(new HashMap<>(other.targets));
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

    public Map<Collectable, Integer> getTargets() {
        return targets;
    }

    public ObjectiveCardPointMultiplier getCopy(){
        return new CollectableCardPointMultiplier(this);
    }
}

package it.polimi.ingsw.model.cardReleted;

import java.util.Map;

public class GoldCard extends CardInHand{
    final private Map<Resource, Integer> requirements;
    final private GoldCardPointMultiplier multiplier;

    public GoldCard(){
        super();
        requirements = null;
        multiplier = null;
    }
}

package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Codex;

import java.util.Map;

public class GoldCard extends CardInHand {
    final private Map<Resource, Integer> requirements;
    final private GoldCardPointMultiplier multiplier;

    /**
     * The constructor of the class
     * @param points set the number of points given by the card
     * @param requirements it's a map of the requirements needed by the card and the number of them
     * @param multiplier define
     * @param frontCorner the front corners map
     * @param permanentResource the resource in the middle of the card's back
     */
    public GoldCard(int points, Map<Resource, Integer> requirements, GoldCardPointMultiplier multiplier, Map<CardCorner, Collectable> frontCorner, Resource permanentResource){
        super(permanentResource, points, frontCorner);
        this.requirements = requirements;
        this.multiplier = multiplier;
    }

    /**
     * @return the requirements for placing the card
     */
    public Map<Resource, Integer> getRequirements() {
        return requirements;
    }

    @Override
    /*
     * @return the amount of point given to the player by the card
     */
    public int getPoints(Codex codex) {
        return this.getPoints()*multiplier.getMultiplier(codex);
    }
}

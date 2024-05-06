package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Codex;

import java.util.HashMap;
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
    public GoldCard(int id, int points, Map<Resource, Integer> requirements, GoldCardPointMultiplier multiplier, Map<CardCorner, Collectable> frontCorner, Resource permanentResource){
        super(id, permanentResource, points, frontCorner);
        this.requirements = requirements;
        this.multiplier = multiplier;
    }

    /**
     * The copy constructor of the class
     * @param other the card to copy
     */
    public GoldCard(GoldCard other){
        this(other.getId(), other.getPoints(), other.getRequirements(), other.getGoldCardPointMultiplier(), other.getFrontCorners(), other.getPermanentResources(CardFace.BACK).stream().findFirst().orElse(null));
    }

    /**
     * @return the requirements for placing the card
     */
    public Map<Resource, Integer> getRequirements() {
        return new HashMap<>(requirements);
    }

    @Override
    /*
     * @return the amount of point given to the player by the card
     */
    public int getPoints(Codex codex) {
        return this.getPoints()*multiplier.getMultiplier(codex, this);
    }

    /**
     * @return the GoldCardPointMultiplier of the card
     */
    public GoldCardPointMultiplier getGoldCardPointMultiplier(){
        return multiplier.getCopy();
    }

    /**
     * @return a copy of the card
     */
    public CardInHand copy() {
        return new GoldCard(this);
    }
}

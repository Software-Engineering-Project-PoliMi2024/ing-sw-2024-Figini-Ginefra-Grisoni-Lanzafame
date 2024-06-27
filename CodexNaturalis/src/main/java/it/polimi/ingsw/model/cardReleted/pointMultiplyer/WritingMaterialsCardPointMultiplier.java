package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;

/**
 * This class is a multiplier for the points given by a gold card
 * that gives points for the number of a specific writing material collected
 */
public class WritingMaterialsCardPointMultiplier implements GoldCardPointMultiplier {
    /** the writing material that the card gives points for*/
    private final WritingMaterial target;

    /** constructor of the writingMaterialsCardPointMultiplier
     * @param target the writing material that the card is referring to*/
    public WritingMaterialsCardPointMultiplier(WritingMaterial target){
        this.target = target;
    }

    /** copy constructor of the writingMaterialsCardPointMultiplier
     * @param other the multiplier to copy*/
    public WritingMaterialsCardPointMultiplier(WritingMaterialsCardPointMultiplier other){
        this(other.target);
    }

    /** @param codex the codex where the card is inserted
     *  @return the multiplicator of the points*/
    @Override
    public int getMultiplier(Codex codex, GoldCard goldCard) {
        if(!codex.getEarnedCollectables().containsKey(target))
            return 0;
        return codex.getEarnedCollectables().get(target);
    }

    /** @return the target of the multiplier*/
    public WritingMaterial getTarget(){
        return target;
    }

    /** @return a copy of the multiplier*/
    public GoldCardPointMultiplier getCopy(){
        return new WritingMaterialsCardPointMultiplier(this);
    }


}

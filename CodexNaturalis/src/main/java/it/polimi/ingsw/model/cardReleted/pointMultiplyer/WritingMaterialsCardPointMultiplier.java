package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;

public class WritingMaterialsCardPointMultiplier implements GoldCardPointMultiplier {
    private final WritingMaterial target;

    /** constructor of the writingMaterialsCardPointMultiplier
     * @param target the writing material that the card is referring to*/
    public WritingMaterialsCardPointMultiplier(WritingMaterial target){
        this.target = target;
    }

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

    public GoldCardPointMultiplier getCopy(){
        return new WritingMaterialsCardPointMultiplier(this);
    }


}

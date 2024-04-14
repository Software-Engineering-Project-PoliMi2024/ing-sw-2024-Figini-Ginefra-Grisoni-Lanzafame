package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

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

    /** @param codex the codex where the card is inserted
     *  @return the multiplicator of the points*/
    @Override
    public int getMultiplier(Codex codex) {
        return codex.getEarnedCollectables().get(target);
    }
}

package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;

import java.io.Serializable;

/**
 * This class is a GoldCardPointMultiplier that multiplies the points of a GoldCard
 */
public interface GoldCardPointMultiplier extends Serializable {
    /**
     * This method returns the multiplier of the points
     * @param codex the codex where the card is placed
     * @param goldCard the gold card that has to be multiplier
     * @return the multiplier of the points
     */
    int getMultiplier(Codex codex, GoldCard goldCard);

    /**
     * @return a copy of the object
     */
    GoldCardPointMultiplier getCopy();

    /**
     * @return the WritingMaterial the player has to collect to activate the multiplier
     */
    WritingMaterial getTarget();
}

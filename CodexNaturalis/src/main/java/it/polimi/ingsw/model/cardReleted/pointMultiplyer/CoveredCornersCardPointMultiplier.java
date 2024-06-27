package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.NoSuchElementException;

/**
 * This class is a GoldCardPointMultiplier that multiplies the points of a GoldCard
 * based on the number of corners covered by other cards
 */
public class CoveredCornersCardPointMultiplier implements GoldCardPointMultiplier {
    /** @param codex the codex where the card is inserted
     *  @return the multiplier of the points*/
    public int getMultiplier(Codex codex, GoldCard goldCard){
        int multiplier = 0;
        Position cardPos = codex.getPlacementHistory().stream()
                .filter(placement -> placement.card()
                        .equals(goldCard)).findFirst().orElseThrow(NoSuchElementException::new).position();

        for (CardCorner corner : CardCorner.values()){
            if(codex.getPlacementAt(cardPos.add(corner.getOffset())) != null)
                multiplier++;
        }
        return multiplier;
    }

    /** @return the WritingMaterial the player has to collect to activate the multiplier*/
    public WritingMaterial getTarget(){
        return null;
    }

    /** @return a copy of the object*/
    public GoldCardPointMultiplier getCopy(){
        return new CoveredCornersCardPointMultiplier();
    }
}

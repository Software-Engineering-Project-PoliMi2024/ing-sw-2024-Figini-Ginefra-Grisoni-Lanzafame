package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.NoSuchElementException;

public class CoveredCornersCardPointMultiplier implements GoldCardPointMultiplier {

    //poco pulito: prendo l'ultima carta nel codex ma non so se è la carta giusta
    /** @param codex the codex where the card is inserted
     *  @return the multiplicator of the points*/
    public int getMultiplier(Codex codex, GoldCard goldCard){
        int multiplier = 0;
        Position cardPos = codex.getPlacementHistory().stream()
                .filter(placement -> placement.card()
                        .equals(goldCard)).findFirst().orElseThrow(NoSuchElementException::new).position();

        for (CardCorner corner : CardCorner.values()){
            if(codex.getPlacementAt(cardPos.add(corner.getOffset())
                    .add(corner.getOffset())) != null)
                multiplier++;
        }
        return multiplier;
    }

    public WritingMaterial getTarget(){
        return null;
    }
}

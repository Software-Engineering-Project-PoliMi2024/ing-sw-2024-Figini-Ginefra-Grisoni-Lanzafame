package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

public class CoveredCornersCardPointMultiplier implements GoldCardPointMultiplier{

    //poco pulito: prendo l'ultima carta nel codex ma non so se è la carta giusta
    /** @param codex the codex where the card is inserted
     *  @return the multiplicator of the points*/
    public int getMultiplier(Codex codex){
        int multiplier = 0;
        Placement p = codex.getPlacementHistory().stream()
                .reduce((first, second) -> second).orElse(null);
        if (p == null)
            throw new NullPointerException("No placements in the codex");

        Position cardPos = p.position();
        for (CardCorner corner : CardCorner.values()){
            if(codex.getPlacementAt(cardPos.add(corner.getOffset())
                    .add(corner.getOffset())) != null)
                multiplier++;
        }
        return multiplier;
    }
}

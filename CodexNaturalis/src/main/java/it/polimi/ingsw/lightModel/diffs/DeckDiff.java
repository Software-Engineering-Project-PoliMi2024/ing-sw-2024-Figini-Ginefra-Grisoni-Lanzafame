package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightDeck;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

public abstract class DeckDiff{
    /**
     * @param lightDeck the deck to which the diff is applied
     */
    public abstract void apply(LightDeck lightDeck);
}
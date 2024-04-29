package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;

public abstract class DeckDiff{
    /**
     * @param lightDeck the deck to which the diff is applied
     */
    public abstract void apply(LightDeck lightDeck);
}
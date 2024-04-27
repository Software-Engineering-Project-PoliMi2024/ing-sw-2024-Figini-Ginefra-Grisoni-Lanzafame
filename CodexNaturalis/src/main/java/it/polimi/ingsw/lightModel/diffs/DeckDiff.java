package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightDeck;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

public abstract class DeckDiff{
    protected final DrawableCard deck;

    /**
     * Constructor of the class
     * @param deck select the deck to which the diff is applied (Resource of Gold)
     */
    public DeckDiff(DrawableCard deck) {
        this.deck = deck;
    }
    /**
     * @param lightDeck the deck to which the diff is applied
     */
    public abstract void apply(LightDeck lightDeck);
}
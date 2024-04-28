package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.lightModel.LightDeck;

public class DeckDiffDeckDraw extends DeckDiff {
    private final Resource resource;
    /**
     * @param deck the deck from which the card is drawn
     * @param resource the resource of the card under the card drawn
     */
    DeckDiffDeckDraw(DrawableCard deck, Resource resource) {
        this.resource = resource;
    }
    /**
     * @param lightDeck the deck to which the diff is applied
     */
    public void apply(LightDeck lightDeck) {
        lightDeck.setTopDeckCard(resource);
    }
}

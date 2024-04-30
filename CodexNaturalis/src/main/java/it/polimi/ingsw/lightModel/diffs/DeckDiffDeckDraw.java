package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class DeckDiffDeckDraw extends DeckDiff {
    private final Resource resource;
    private final DrawableCard deck;
    /**
     * @param deck the deck from which the card is drawn
     * @param resource the resource of the card under the card drawn
     */
    public DeckDiffDeckDraw(DrawableCard deck, Resource resource) {
        this.resource = resource;
        this.deck = deck;
    }
    /**
     * @param ligthgame the game to which the diff is applied
     */
    public void apply(LightGame ligthgame) {
        ligthgame.setTopDeck(resource, deck);
    }
}

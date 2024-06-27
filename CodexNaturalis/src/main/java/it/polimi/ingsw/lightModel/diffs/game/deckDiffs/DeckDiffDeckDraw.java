package it.polimi.ingsw.lightModel.diffs.game.deckDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

/**
 * This class contains the update to the lightModel for showing a new backCard in the deck after a draw
 */
public class DeckDiffDeckDraw extends DeckDiff {
    /** the new back of the top card of the deck */
    private final LightBack back;
    /** the deck from which the card is drawn */
    private final DrawableCard deck;
    /**
     * @param deck the deck from which the card is drawn
     * @param back the new back of the top card of the deck
     */
    public DeckDiffDeckDraw(DrawableCard deck, LightBack back) {
        this.back = back;
        this.deck = deck;
    }
    /**
     * @param ligthgame the game to which the diff is applied
     */
    public void apply(LightGame ligthgame) {
        ligthgame.setTopDeck(back, deck);
    }
}

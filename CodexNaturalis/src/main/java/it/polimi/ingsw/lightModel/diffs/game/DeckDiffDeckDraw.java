package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class DeckDiffDeckDraw extends DeckDiff {
    private final LightBack back;
    private final DrawableCard deck;
    /**
     * @param deck the deck from which the card is drawn
     * @param back the back of the card under the card drawn
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

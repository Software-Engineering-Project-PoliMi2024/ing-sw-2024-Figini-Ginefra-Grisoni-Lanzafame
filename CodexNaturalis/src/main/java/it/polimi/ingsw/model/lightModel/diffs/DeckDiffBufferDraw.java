package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightDeck;

public class DeckDiffBufferDraw extends DeckDiff{
    private final LightCard card;
    private final Integer position;
    DeckDiffBufferDraw(DrawableCard deck, LightCard card, Integer position) {
        super(deck);
        this.card = card;
        this.position = position;
    }
    public void apply(LightDeck lightDeck) {
        if(super.deck == DrawableCard.GOLDCARD) {
            LightCard[] tmp = lightDeck.getGoldCardBuffer();
            tmp[position] = card;
            lightDeck.setGoldCardBuffer(tmp);
            lightDeck.getGoldCardBuffer()[position] = card;
        }else if(super.deck == DrawableCard.RESOURCECARD){
            LightCard[] tmp = lightDeck.getResourceCardBuffer();
            tmp[position] = card;
            lightDeck.setResourceCardBuffer(tmp);
            lightDeck.getResourceCardBuffer()[position] = card;
        }
    }
}

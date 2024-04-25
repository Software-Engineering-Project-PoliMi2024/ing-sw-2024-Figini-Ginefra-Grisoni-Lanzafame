package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.lightModel.LightDeck;

public class DeckDiffDeckDraw extends DeckDiff {
    private final Resource resource;
    DeckDiffDeckDraw(DrawableCard deck, Resource resource) {
        super(deck);
        this.resource = resource;
    }
    public void apply(LightDeck lightDeck) {
        if(super.deck == DrawableCard.GOLDCARD) {
            lightDeck.setGoldCardDeck(resource);
        }else if(super.deck == DrawableCard.RESOURCECARD){
            lightDeck.setResourceCardDeck(resource);
        }
    }
}

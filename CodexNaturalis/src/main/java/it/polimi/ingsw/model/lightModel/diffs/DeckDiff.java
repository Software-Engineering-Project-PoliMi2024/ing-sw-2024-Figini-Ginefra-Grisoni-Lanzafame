package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightDeck;

public abstract class DeckDiff{
    protected final DrawableCard deck;
    public abstract void apply(LightDeck lightDeck);
    public DeckDiff(DrawableCard deck) {
        this.deck = deck;
    }
}
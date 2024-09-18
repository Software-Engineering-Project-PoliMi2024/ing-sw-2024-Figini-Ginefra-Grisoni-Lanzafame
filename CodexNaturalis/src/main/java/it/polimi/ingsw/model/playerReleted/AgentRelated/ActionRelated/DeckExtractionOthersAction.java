package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DeckExtractionOthersAction extends DeckExtractionAction {
    public DeckExtractionOthersAction(CardInHand extractedCard) {
        super(extractedCard);
    }

    @Override
    public void actOnState(State state) {
        if(state.getResourceDeckBelief().bufferSize() != 2 || state.getGoldDeckBelief().bufferSize() != 2){
            switch (state.getDrawnFrom()){
                case RESOURCECARD:
                    state.getResourceDeckBelief().addToBufferInEmptySlot((ResourceCard) extractedCard);
                    state.setDrawnFrom(null);
                    break;
                case GOLDCARD:
                    state.getGoldDeckBelief().addToBufferInEmptySlot((GoldCard) extractedCard);
                    state.setDrawnFrom(null);
                    break;
                default:
                    throw new IllegalStateException("DrawnFrom is null");
            }
        }
    }
}

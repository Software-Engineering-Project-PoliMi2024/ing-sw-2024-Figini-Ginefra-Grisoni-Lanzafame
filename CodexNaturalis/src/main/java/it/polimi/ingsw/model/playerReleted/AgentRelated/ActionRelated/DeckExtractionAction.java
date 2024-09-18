package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DeckExtractionAction implements Action{
    protected final CardInHand extractedCard;

    public DeckExtractionAction(CardInHand extractedCard){
        this.extractedCard = extractedCard;
    }
    @Override
    public void actOnState(State state) {
        if(state.getHand().getHandSize() < 3){
            state.getHand().addCard(extractedCard);
        }
        else{
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
                    throw new IllegalStateException("The hand is full and both buffers are full");
            }
        }
    }

    @Override
    public void actOnGame(GameController controller, String nickname) {
        throw new UnsupportedOperationException("A deck extraction action cannot be executed on the game controller");
    }

    @Override
    public float getSelectionScore(Node node) {
        return 0;
    }

    @Override
    public int simulate(State state) {
        return 0;
    }
}

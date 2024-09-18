package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DrawAction implements Action{
    private final DrawableCard targetDeck;
    private final int targetPosition;

    public DrawAction(DrawableCard targetDeck, int targetPosition){
        this.targetDeck = targetDeck;
        this.targetPosition = targetPosition;
    }

    @Override
    public void actOnState(State state) {
        state.setDrawnFrom(targetDeck);

        if(targetPosition == Configs.actualDeckPos)
            return;

        switch(targetDeck){
            case DrawableCard.RESOURCECARD:
                ResourceCard drawnCard = state.getResourceDeckBelief().drawFromBufferNoReplace(targetPosition);
                state.getHand().addCard(drawnCard);
                break;
            case DrawableCard.GOLDCARD:
                GoldCard drawnGold = state.getGoldDeckBelief().drawFromBufferNoReplace(targetPosition);
                state.getHand().addCard(drawnGold);
                break;
        }
    }

    @Override
    public void actOnGame(GameController controller, String nickname) {
        controller.draw(nickname, targetDeck, targetPosition);
    }

    @Override
    public float getSelectionScore(Node node) {
        return UCB1.score(node);
    }

    @Override
    public int simulate(State state) {
        return 0;
    }
}

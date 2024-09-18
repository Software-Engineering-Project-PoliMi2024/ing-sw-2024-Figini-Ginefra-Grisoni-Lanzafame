package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;
import it.polimi.ingsw.model.playerReleted.Placement;

public class PlaceAction implements Action {
    private final Placement placement;

    public PlaceAction(Placement placement) {
        this.placement = placement;
    }

    @Override
    public void actOnState(State state) {
        state.getHand().removeCard((CardInHand) placement.card());
        state.getCodex().playCard(placement);
    }

    @Override
    public void actOnGame(GameController controller, String nickname) {
        controller.place(nickname, Lightifier.lightify(placement));
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

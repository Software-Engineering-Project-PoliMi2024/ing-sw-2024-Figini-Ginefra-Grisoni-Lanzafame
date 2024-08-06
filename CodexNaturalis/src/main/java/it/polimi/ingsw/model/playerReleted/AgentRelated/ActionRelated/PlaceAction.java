package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

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
    public int getSelectionScore(Node node) {
        return 0;
    }

    @Override
    public int simulate(State state) {
        return 0;
    }
}

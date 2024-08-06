package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

import java.util.function.Predicate;

public interface Action {
    void actOnState(State state);
    void actOnGame(GameController controller, String nickname);
    int getSelectionScore(Node node);
    int simulate(State state);
}

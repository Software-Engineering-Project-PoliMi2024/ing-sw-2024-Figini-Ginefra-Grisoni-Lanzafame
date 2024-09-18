package it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public interface Action {
    void actOnState(State state);
    void actOnGame(GameController controller, String nickname);
    float getSelectionScore(Node node);
    int simulate(State state);
}

package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public interface Expander {
    Node expand(Node node, State state);
}

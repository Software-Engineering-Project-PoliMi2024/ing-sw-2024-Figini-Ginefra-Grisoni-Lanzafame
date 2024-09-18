package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DeckExtractionOthersAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DrawOthersAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DrawOthersExpander extends DrawExpander{
    final int othersConsidered;
    public DrawOthersExpander(State state, int othersConsidered){
        super(state);
        this.othersConsidered = othersConsidered;
    }

    @Override
    public Node expand(Node node, State state) {
        Node child = new Node(
                node,
                new DrawOthersAction(
                        DrawableCard.values()[deckOptionIndex],
                        positionIndex
                ),
                node.getDepth() + 1,
                new DeckExtractionOthersExpander(state, DrawableCard.values()[deckOptionIndex], othersConsidered)
        );
        tick();
        if(deckOptionIndex == deckOptionSize){
            node.setExpandable(false);
        }
        return child;
    }
}

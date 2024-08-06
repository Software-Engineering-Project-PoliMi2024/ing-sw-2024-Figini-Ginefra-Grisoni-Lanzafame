package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DrawAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DrawExpander implements Expander{
    private int deckOptionIndex = 0;
    private int positionIndex = 0;
    private final int deckOptionSize;
    private final int positionSize;

    public DrawExpander(State state){
        deckOptionSize = 2;
        positionSize = 3;
    }

    private void tick(){
        positionIndex++;
        deckOptionIndex += positionIndex / positionSize;
        positionIndex %= positionSize;
    }

    @Override
    public Node expand(Node node, State state) {
        Node child = new Node(
                node,
                new DrawAction(
                        DrawableCard.values()[deckOptionIndex],
                        positionIndex
                ),
                node.getDepth() + 1,
                new DeckExtractionExpander(state));
        tick();
        if(deckOptionIndex == deckOptionSize){
            node.setExpandable(false);
        }
        return child;
    }
}

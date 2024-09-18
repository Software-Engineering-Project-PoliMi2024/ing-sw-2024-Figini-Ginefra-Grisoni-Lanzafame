package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DrawAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DrawExpander implements Expander{
    protected int deckOptionIndex = 0;
    protected int positionIndex = 0;
    protected final int deckOptionSize;
    protected final int positionSize;

    public DrawExpander(State state){
        deckOptionSize = 2;
        positionSize = 3;
    }

    protected void tick(){
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
                new DeckExtractionExpander(state, DrawableCard.values()[deckOptionIndex])
        );
        tick();
        if(deckOptionIndex == deckOptionSize){
            node.setExpandable(false);
        }
        return child;
    }
}

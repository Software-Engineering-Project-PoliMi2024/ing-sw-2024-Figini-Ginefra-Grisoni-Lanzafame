package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.PlaceAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated.Expander;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

public class PlaceExpander implements Expander {
    private int frontierIndex = 0;
    private int faceIndex = 0;
    private int handIndex = 0;

    private final int frontierSize;
    private final int faceSize;
    private final int handSize;

    public PlaceExpander(State state){
        frontierSize = state.getCodex().getFrontier().size();
        faceSize = CardFace.values().length;
        handSize = state.getHand().getHandSize();

        CardInHand card = state.getHand().getHand().stream().toList().get(handIndex);
        CardFace face = CardFace.values()[faceIndex];

        while(face == CardFace.FRONT && !card.canBePlaced(state.getCodex()) && frontierIndex < frontierSize){
            tick();
        }

    }

    public void setExpandable(Node node){
        node.setExpandable(frontierIndex < frontierSize);
    }


    private void tick(){
        handIndex++;

        faceIndex += handIndex / handSize;
        handIndex %= handSize;

        frontierIndex += faceIndex / faceSize;
        faceIndex %= faceSize;
    }
    @Override
    public Node expand(Node node, State state) {

        Position position = state.getCodex().getFrontier().getFrontier().get(frontierIndex);
        CardInHand card = state.getHand().getHand().stream().toList().get(handIndex);
        CardFace face = CardFace.values()[faceIndex];

        Placement placement = new Placement(position, card, face);

        Node child = new Node(
                node,
                new PlaceAction(placement),
                node.getDepth() + 1,
                new DrawExpander(state)
        );


        tick();

        while(face == CardFace.FRONT && !card.canBePlaced(state.getCodex()) && frontierIndex < frontierSize){
            tick();
        }

        if(frontierIndex == frontierSize){
            node.setExpandable(false);
        }

        return child;
    }
}

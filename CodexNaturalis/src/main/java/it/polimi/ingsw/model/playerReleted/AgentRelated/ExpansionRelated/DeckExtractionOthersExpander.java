package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DeckExtractionAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DeckExtractionOthersAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DeckExtractionOthersExpander extends DeckExtractionExpander{

    private final int othersConsidered;

    public DeckExtractionOthersExpander(State state, DrawableCard targetDeck, int othersConsidered){
        super(state, targetDeck);
        this.othersConsidered = othersConsidered;
    }

    @Override
    public Node expand(Node node, State state) {
        CardInHand extractedCard = switch (targetDeck) {
            case RESOURCECARD -> state.getResourceDeckBelief().getCardLeftAt(extractedCardIndex);
            case GOLDCARD -> state.getGoldDeckBelief().getCardLeftAt(extractedCardIndex);
            default -> throw new IllegalStateException("Unexpected value: " + targetDeck);
        };

        Expander nextExpander;
        if(othersConsidered == state.getNPlayers() -2)
            nextExpander = new PlaceExpander(state);
        else
            nextExpander = new DrawOthersExpander(state, othersConsidered + 1);

        Node child = new Node(
                node,
                new DeckExtractionAction(
                        extractedCard
                ),
                node.getDepth() + 1,
                nextExpander);

        if(othersConsidered == state.getNPlayers() - 2){
            assert nextExpander instanceof PlaceExpander;
            ((PlaceExpander)nextExpander).setExpandable(child);
        }

        tick();
        if(extractedCardIndex == deckSize){
            node.setExpandable(false);
        }
        return child;
    }
}

package it.polimi.ingsw.model.playerReleted.AgentRelated.ExpansionRelated;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DeckExtractionAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.ActionRelated.DrawAction;
import it.polimi.ingsw.model.playerReleted.AgentRelated.Node;
import it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated.State;

public class DeckExtractionExpander implements Expander{
    protected final DrawableCard targetDeck;
    protected int extractedCardIndex = 0;
    protected final int deckSize;

    public DeckExtractionExpander(State state, DrawableCard targetDeck){
        this.targetDeck = targetDeck;
        deckSize = targetDeck == DrawableCard.RESOURCECARD ? state.getResourceDeckBelief().numCardsLeft() : state.getGoldDeckBelief().numCardsLeft();
    }

    protected void tick(){
        extractedCardIndex++;
    }

    @Override
    public Node expand(Node node, State state) {
        CardInHand extractedCard = switch (targetDeck) {
            case RESOURCECARD -> state.getResourceDeckBelief().getCardLeftAt(extractedCardIndex);
            case GOLDCARD -> state.getGoldDeckBelief().getCardLeftAt(extractedCardIndex);
            default -> throw new IllegalStateException("Unexpected value: " + targetDeck);
        };

        Expander nextExpander = new DeckExtractionOthersExpander(state, targetDeck, 0);

        Node child = new Node(
                node,
                new DeckExtractionAction(
                        extractedCard
                ),
                node.getDepth() + 1,
                nextExpander
        );

        tick();
        if(extractedCardIndex == deckSize){
            node.setExpandable(false);
        }
        return child;
    }
}

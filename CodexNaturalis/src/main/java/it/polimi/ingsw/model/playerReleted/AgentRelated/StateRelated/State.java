package it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.tableReleted.Deck;

public class State {
    private final Codex codex;
    private final Hand hand;
    private final DeckBelief<ResourceCard> resourceDeckBelief;
    private final DeckBelief<GoldCard> goldDeckBelief;

    public State(Codex codex, Hand hand, DeckBelief<ResourceCard> resourceCardDeckBelief, DeckBelief<GoldCard> goldCardDeckBelief) {
        this.codex = codex;
        this.hand = hand;
        this.resourceDeckBelief = resourceCardDeckBelief;
        this.goldDeckBelief = goldCardDeckBelief;
    }

    public State(Player targetPlayer, DeckBelief<ResourceCard> resourceCardDeckBelief, DeckBelief<GoldCard> goldCardDeckBelief) {
        this.codex = new Codex(targetPlayer.getUserCodex());
        this.hand = new Hand(targetPlayer.getUserHand());
        this.resourceDeckBelief = resourceCardDeckBelief;
        this.goldDeckBelief = goldCardDeckBelief;
    }
}

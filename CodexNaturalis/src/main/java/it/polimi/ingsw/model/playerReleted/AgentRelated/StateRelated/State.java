package it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.tableReleted.Deck;

import java.util.List;

public class State {
    private final Codex codex;
    private final Hand hand;
    private final List<ObjectiveCard> commonObjectives;
    private final DeckBelief<ResourceCard> resourceDeckBelief;
    private final DeckBelief<GoldCard> goldDeckBelief;
    private final String nickname;

    private boolean isLastTurn = false;

    public State(Codex codex, Hand hand, DeckBelief<ResourceCard> resourceCardDeckBelief, DeckBelief<GoldCard> goldCardDeckBelief, List<ObjectiveCard> commonObjectives, String nickname) {
        this.codex = codex;
        this.hand = hand;
        this.resourceDeckBelief = resourceCardDeckBelief;
        this.goldDeckBelief = goldCardDeckBelief;
        this.commonObjectives = commonObjectives;
        this.nickname = nickname;
    }

    public State(Player targetPlayer, DeckBelief<ResourceCard> resourceCardDeckBelief, DeckBelief<GoldCard> goldCardDeckBelief, List<ObjectiveCard> commonObjectives) {
        this.codex = new Codex(targetPlayer.getUserCodex());
        this.hand = new Hand(targetPlayer.getUserHand());
        this.resourceDeckBelief = resourceCardDeckBelief;
        this.goldDeckBelief = goldCardDeckBelief;
        this.commonObjectives = commonObjectives;
        this.nickname = targetPlayer.getNickname();
    }

    public State(State other){
        this.codex = new Codex(other.codex);
        this.hand = new Hand(other.hand);
        this.resourceDeckBelief = new DeckBelief<>(other.resourceDeckBelief);
        this.goldDeckBelief = new DeckBelief<>(other.goldDeckBelief);
        this.commonObjectives = other.commonObjectives;
        this.isLastTurn = other.isLastTurn;
        this.nickname = other.nickname;
    }

    public int computePoints(){
        int codexPoints = codex.getPoints();
        int secretObjectivePoints = hand.getSecretObjective().getPoints(codex);
        int commonObjectivePoints = commonObjectives.stream().map(o -> o.getPoints(codex)).reduce(0, Integer::sum);
        return codexPoints + secretObjectivePoints + commonObjectivePoints;
    }

    public boolean isTerminal(){
        boolean isTerminal = !resourceDeckBelief.canDraw() || !goldDeckBelief.canDraw() || codex.getFrontier().size() == 0 || isLastTurn;
        if(codex.getPoints() >= 20)
            isLastTurn = true;
        return isTerminal;
    }

    public Hand getHand() {
        return hand;
    }

    public Codex getCodex() {
        return codex;
    }

    public DeckBelief<ResourceCard> getResourceDeckBelief() {
        return resourceDeckBelief;
    }

    public DeckBelief<GoldCard> getGoldDeckBelief() {
        return goldDeckBelief;
    }

    public String getNickname() {
        return nickname;
    }
}

package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.io.Serializable;
import java.util.*;

/**
 * This class is used to convert Object present in the Model to the corresponding LightObject present in the LightModel
 * so that a client can only have the information it needs to display the game
 */
public class Lightifier implements Serializable {
    /**
     * @param lobby which will be lightened
     * @return a LightLobby containing the PlayerList, the name and the max number of player
     */
    public static LightLobby lightify(Lobby lobby) {
        return new LightLobby(lobby.getLobbyPlayerList(), lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
    }

    /**
     * transforms a list of lobbies into a list of lightLobbies
     * @param lobbies the list of lobbies to be lightened
     * @return a list of LightLobby
     */
    public static List<LightLobby> lightify(List<Lobby> lobbies){
        List<LightLobby> lightLobbies = new ArrayList<>();
        for(Lobby lobby : lobbies){
            lightLobbies.add(lightify(lobby));
        }
        return lightLobbies;
    }

    /**
     * @param codex which will be lightened
     * @return a LightCodex containing points, collectable, a lightFrontier and a map of LightPlacement,
     */
    public static LightCodex lightify(Codex codex){
        Map<Position, LightPlacement> lightPlacementMap = new HashMap<>();
        for(Placement placement : codex.getPlacementHistory()){
            lightPlacementMap.put(placement.position(), lightify(placement));
        }
        return new LightCodex(codex.getPoints(), codex.getEarnedCollectables(), lightify(codex.getFrontier()), lightPlacementMap);
    }

    /**
     * @param frontier which will be lightened
     * @return a LightFrontier containing only the frontier without all the other methods
     */
    public static LightFrontier lightify(Frontier frontier){
        return new LightFrontier(frontier.getFrontier());
    }

    /**
     * @param placement which will be lightened
     * @return a LighPlacement containing only the placement position, and a lightCard
     */
    public static LightPlacement lightify(Placement placement){
        return new LightPlacement(placement.position(), lightifyToCard(placement.card()), placement.face());
    }

    /**
     * @param card which will be lightened
     * @return a LighCard containing only the CardID
     */
    public static LightCard lightifyToCard(Card card){
        if(card == null)
            return null;
        else
            return new LightCard(card.getIdFront(), card.getIdBack());
    }

    /**
     * @param codex from which get the playability
     * @param hand the hand which will be lightened
     * @return a LightHand containing all the (light)CardInHand, the lightSecretObjective and each card playability
     */
    public static LightHand lightifyYour(Hand hand, Codex codex){
        HashMap<LightCard, Boolean> cardPlayability = new HashMap<>();
        for(CardInHand card : hand.getHand()){
            cardPlayability.put(lightifyToCard(card), card.canBePlaced(codex));
        }

        LightHand h = new LightHand(cardPlayability);
        if(hand.getSecretObjective() != null)
            h.setSecretObjective(lightifyToCard(hand.getSecretObjective()));

        List<ObjectiveCard> objectiveOptions = hand.getSecretObjectiveChoices();
        if(objectiveOptions != null){
            h.addSecretObjectiveOption(Lightifier.lightifyToCard(objectiveOptions.getFirst()));
            h.addSecretObjectiveOption(Lightifier.lightifyToCard(objectiveOptions.getLast()));
        }

        return h;
    }

    /**
     * @param hand which will be lightened
     * @return a LightHandOther containing the permanent resource of each CardInHand of the other player
     */
    public static LightHandOthers lightifyOthers(Hand hand){
        LightHandOthers handOthers = new LightHandOthers();
        for(CardInHand card : hand.getHand()){
            handOthers.addCard(new LightBack(card.getIdBack()));
        }
       return handOthers;
    }

    /**
     * Given a card, it returns a LightBack containing the idBack of that card
     * @param card the card to be lightened
     * @return a LightBack containing the idBack of the card
     */
    public static LightBack lightifyToBack(Card card){
        if(card == null)
            return null;
        else{
            return new LightBack(card.getIdBack());
        }

    }
}

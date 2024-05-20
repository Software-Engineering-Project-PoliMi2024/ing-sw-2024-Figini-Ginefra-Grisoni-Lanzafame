package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.LobbyList;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Lightifier implements Serializable {
    /**
     * @param lobby which will be lightened
     * @return a LightLobby containing the PlayerList and the name of the lobby
     */
    public static LightLobby lightify(Lobby lobby) {
        return new LightLobby(lobby.getLobbyPlayerList(), lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
    }
    public static LightLobbyList lightify(LobbyList lobbies){
        return new LightLobbyList(lobbies.getLobbies().stream().map(Lightifier::lightify).toList());
    }

    /**
     * @param codex which will be lightened
     * @return a LightCodex containing points, collectable, a lightFrontier and a list of LightPlacement,
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
     * @return a LighFrontier
     */
    public static LightFrontier lightify(Frontier frontier){
        return new LightFrontier(frontier.getFrontier());
    }

    /**
     * @param placement which will be lightened
     * @return a LighPlacement
     */
    public static LightPlacement lightify(Placement placement){
        return new LightPlacement(placement.position(), lightifyToCard(placement.card()), placement.face());
    }

    /**
     * @param card which will be lightened
     * @return a LighCard containing only the CardID
     */
    public static LightCard lightifyToCard(Card card){
        return new LightCard(card.getIdFront(), card.getIdBack());
    }
    /**
     * @param card which will be lightened
     * @return the permanentResource on the back of the card
     */
    public static Resource lightifyToResource(CardInHand card){
        return card.getPermanentResources(CardFace.BACK).stream().toList().getFirst();
    }

    /**
     * @param hand which will be lightened
     * @return a LighHand containing all the (light)CardInHand, the lightSecretObjective and each card playbility
     */
    public static LightHand lightifyYour(Hand hand, User user){
        HashMap<LightCard, Boolean> cardPlayability = new HashMap<>();
        for(CardInHand card : hand.getHand()){
            cardPlayability.put(lightifyToCard(card), card.canBePlaced(user.getUserCodex()));
        }
        LightHand h = new LightHand(cardPlayability);
        if(hand.getSecretObjective() != null)
            h.setSecretObjective(lightifyToCard(hand.getSecretObjective()));
        return h;
    }

    /**
     * @param hand which will be lightened
     * @return a LightHandOther containing the permanent resource of each CardInHand of the other player
     */
    public static LightHandOthers lightifyOthers(Hand hand){
        //getPermanentResources(CardFace.BACK) returns a list set of Resource for each CardInHand. This set is then flattened and converted to an array
       return new LightHandOthers(hand.getHand().stream().flatMap(CardInHand -> CardInHand.getPermanentResources(CardFace.BACK).stream()).toArray(Resource[]::new));
    }
}

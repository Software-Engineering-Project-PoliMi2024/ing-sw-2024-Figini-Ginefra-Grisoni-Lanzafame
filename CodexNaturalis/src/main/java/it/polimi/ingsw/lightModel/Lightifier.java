package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.lightModel.diffs.CodexDiff;
import it.polimi.ingsw.lightModel.diffs.HandDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.LobbyList;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Lightifier implements Serializable {
    public static LightLobby lightify(Lobby lobby) {
        return new LightLobby(lobby.getLobbyPlayerList(), lobby.getLobbyName());
    }
    public static LightLobbyList lightify(LobbyList lobbies){
        return new LightLobbyList(lobbies.getLobbies().stream().map(Lightifier::lightify).toList());
    }
    public static LightCodex lightify(Codex codex){
        Map<Position, LightPlacement> lightPlacementMap = new HashMap<>();
        for(Placement placement : codex.getPlacementHistory()){
            lightPlacementMap.put(placement.position(), lightify(placement));
        }
        return new LightCodex(codex.getPoints(), codex.getEarnedCollectables(), lightify(codex.getFrontier()), lightPlacementMap);
    }
    public static LightFrontier lightify(Frontier frontier){
        return new LightFrontier(frontier.getFrontier());
    }
    public static LightPlacement lightify(Placement placement){
        return new LightPlacement(placement.position(), lightifyToCard(placement.card()), placement.face());
    }

    public static LightCard lightifyToCard(Card card){
        return new LightCard(card.getId());
    }
    public static Resource lightifyToResource(CardInHand card){return card.getPermanentResources(CardFace.BACK).stream().toList().getFirst();}
    public static LightHand lightifyYour(Hand hand){
        HashMap<LightCard, Boolean> cardPlayability = new HashMap<>();
        for(CardInHand card : hand.getHand()){
            cardPlayability.put(lightifyToCard(card), card.canBePlaced());
        }
        return new LightHand(lightifyToCard(hand.getSecretObjective()), cardPlayability);
    }

    public static LightHandOthers lightifyOthers(Hand hand){
       return new LightHandOthers(hand.getHand().stream().map(CardInHand -> CardInHand.getPermanentResources(CardFace.BACK)).toArray(Resource[]::new));
    }
}

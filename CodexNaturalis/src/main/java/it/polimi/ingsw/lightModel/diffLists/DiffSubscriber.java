package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.controller2.ViewInterface;
import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.util.Map;

public interface DiffSubscriber extends ViewInterface {
    String getNickname();
    LightLobbyList getLobbyList();
    //table name identifies both the lobby and the game
    String getTableName();
    //overload to have the equality checked on nicknames
    boolean equals(Object obj);
    Map<String, Boolean> getGamePlayerList();
    Map<DrawableCard, LightDeck> getDeckMap();
    String getCurrentPlayer();
    LightCodex getCodex(String owner);
    LightHand getYourHand();
    LightHandOthers getHandOthers(String owner);
    LightCard getSecretObjective();
    LightCard[] getPublicObjective();
}

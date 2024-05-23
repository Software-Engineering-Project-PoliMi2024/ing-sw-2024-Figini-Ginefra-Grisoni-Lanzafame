package it.polimi.ingsw.controller3.mediators;

import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightLobbyListUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import javax.xml.transform.Templates;


public class LobbyListMediator extends Mediator<LightLobbyListUpdater, LightLobbyList> {
    /**
     * Notifies all the subscribers that a lobby has been added
     * Adds the lobby to the lightLobbyList connected to the updater
     * Sends the diff passed as parameter and logs the event
     * @param creator the subscriber that created the lobby
     * @param diff the diff of the lightLobbyList that adds the lobby
     */
    public void notifyNewLobby(String creator, ModelDiffs<LightLobbyList> diff){
        subscribers.forEach((nickname, pair) -> {
            try {
                pair.first().updateLobbyList(diff);
                if(!nickname.equals(creator))
                    pair.second().log(LogsOnClientStatic.LOBBY_CREATED_OTHERS);
                else
                    pair.second().log(LogsOnClientStatic.LOBBY_CREATED_YOU);
            } catch (Exception e) {
                System.out.println("LobbyListMediator: error in notifying about the new lobby" + e.getMessage());
            }
        });
    }

    /**
     * Notifies the subscriber that removed the lobby
     * Removes the lobby from the lightLobbyList connected to the updater
     * @param destroyer the subscriber that removed the lobby
     * @param diff the diff that removes the lobby from the lightLobbyList on client
     */
    public void notifyLobbyRemoved(String destroyer, ModelDiffs<LightLobbyList> diff){
        subscribers.forEach((nickname, pair) -> {
            try {
                pair.first().updateLobbyList(diff);
                if(nickname.equals(destroyer))
                    pair.second().log(LogsOnClientStatic.LOBBY_REMOVED_YOU);
            } catch (Exception e) {
                System.out.println("LobbyListMediator: error in notifying about the removed lobby" + e.getMessage());
            }
        });
    }

}

package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.controller4.GameController;
import it.polimi.ingsw.controller4.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller4.Interfaces.ReceptionControllerInterface;
import it.polimi.ingsw.controller4.LobbyController;
import it.polimi.ingsw.controller4.LobbyGameListController;
import it.polimi.ingsw.controller4.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the controller that handles the reception of the clients. It manages the lobbies, the nicknames and the offline games
 */
public class LobbyListControllerPublic {
    public LobbyGameListController lobbyGameListController;

    public LobbyListControllerPublic(LobbyGameListController lobbyGameListController) {
        this.lobbyGameListController = lobbyGameListController;
    }

    public CardTable getCardTable() {
        try {
            Field field = lobbyGameListController.getClass().getDeclaredField("cardTable");
            field.setAccessible(true);
            return (CardTable) field.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field field = lobbyGameListController.getClass().getDeclaredField("viewMap");
            field.setAccessible(true);
            return (Map<String, ViewInterface>) field.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, LobbyController> getLobbyMap() {
        try {
            Field field = lobbyGameListController.getClass().getDeclaredField("lobbyMap");
            field.setAccessible(true);
            return (Map<String, LobbyController>) field.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, GameController> getGameMap() {
        try {
            Field field = lobbyGameListController.getClass().getDeclaredField("gameMap");
            field.setAccessible(true);
            return (Map<String, GameController>) field.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

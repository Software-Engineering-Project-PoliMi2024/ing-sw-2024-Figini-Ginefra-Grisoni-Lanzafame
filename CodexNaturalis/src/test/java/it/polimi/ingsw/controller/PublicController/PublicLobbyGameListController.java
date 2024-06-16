package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.controller.LobbyGameListsController;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.view.ViewInterface;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is the controller that handles the reception of the clients. It manages the lobbies, the nicknames and the offline games
 */
public class PublicLobbyGameListController {
    public LobbyGameListsController lobbyGameListController;

    public PublicLobbyGameListController(LobbyGameListsController lobbyGameListController) {
        this.lobbyGameListController = lobbyGameListController;
    }

    public CardTable getCardTable() {
        try {
            Field cardTableField = lobbyGameListController.getClass().getDeclaredField("cardTable");
            cardTableField.setAccessible(true);
            return (CardTable) cardTableField.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field viewMapField = lobbyGameListController.getClass().getDeclaredField("viewMap");
            viewMapField.setAccessible(true);
            return (Map<String, ViewInterface>) viewMapField.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, LobbyController> getLobbyMap() {
        try {
            Field lobbyMapField = lobbyGameListController.getClass().getDeclaredField("lobbyMap");
            lobbyMapField.setAccessible(true);
            return (Map<String, LobbyController>) lobbyMapField.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, GameController> getGameMap() {
        try {
            Field gameMapField = lobbyGameListController.getClass().getDeclaredField("gameMap");
            gameMapField.setAccessible(true);
            return (Map<String, GameController>) gameMapField.get(lobbyGameListController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

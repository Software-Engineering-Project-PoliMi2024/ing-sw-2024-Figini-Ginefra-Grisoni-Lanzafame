package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;

import java.lang.reflect.Field;
import java.util.Map;

public class PublicLobbyController {
    public LobbyController lobbyController;

    public PublicLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public Lobby getLobby() {
        try {
            Field lobbyField = lobbyController.getClass().getDeclaredField("lobby");
            lobbyField.setAccessible(true);
            return (Lobby) lobbyField.get(lobbyController);
        } catch (Exception e) {
            Configs.printStackTrace(e);
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field viewMapField = lobbyController.getClass().getDeclaredField("viewMap");
            viewMapField.setAccessible(true);
            return (Map<String, ViewInterface>) viewMapField.get(lobbyController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, GameControllerReceiver> getGameReceiverMap() {
        try {
            Field gameReceiverMapField = lobbyController.getClass().getDeclaredField("gameReceiverMap");
            gameReceiverMapField.setAccessible(true);
            return (Map<String, GameControllerReceiver>) gameReceiverMapField.get(lobbyController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


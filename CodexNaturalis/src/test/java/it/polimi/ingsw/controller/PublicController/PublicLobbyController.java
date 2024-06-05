package it.polimi.ingsw.controller.PublicController;

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
            Field field = lobbyController.getClass().getDeclaredField("lobby");
            field.setAccessible(true);
            return (Lobby) field.get(lobbyController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field field = lobbyController.getClass().getDeclaredField("viewMap");
            field.setAccessible(true);
            return (Map<String, ViewInterface>) field.get(lobbyController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, GameControllerReceiver> getGameReceiverMap() {
        try {
            Field field = lobbyController.getClass().getDeclaredField("gameReceiverMap");
            field.setAccessible(true);
            return (Map<String, GameControllerReceiver>) field.get(lobbyController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


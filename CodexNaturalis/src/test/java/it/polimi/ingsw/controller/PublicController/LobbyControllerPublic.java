package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.controller4.GameController;
import it.polimi.ingsw.controller4.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller4.LobbyController;
import it.polimi.ingsw.controller4.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LobbyControllerPublic {
    public LobbyController lobbyController;

    public LobbyControllerPublic(LobbyController lobbyController) {
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


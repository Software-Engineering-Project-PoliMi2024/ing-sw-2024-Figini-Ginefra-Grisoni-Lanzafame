package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.ViewInterface;

import java.lang.reflect.Field;
import java.util.*;

public class PublicGameController {
    public GameController gameController;

    public PublicGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public CardTable getCardTable() {
        try {
            Field cardTableField = gameController.getClass().getDeclaredField("cardTable");
            cardTableField.setAccessible(true);
            return (CardTable) cardTableField.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Game getGame() {
        try {
            Field gameField = gameController.getClass().getDeclaredField("game");
            gameField.setAccessible(true);
            return (Game) gameField.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field playerViewMapField = gameController.getClass().getDeclaredField("playerViewMap");
            playerViewMapField.setAccessible(true);
            return (Map<String, ViewInterface>) playerViewMapField.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

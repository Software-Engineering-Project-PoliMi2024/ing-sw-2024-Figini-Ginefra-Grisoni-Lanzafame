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
            Field field = gameController.getClass().getDeclaredField("cardTable");
            field.setAccessible(true);
            return (CardTable) field.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Game getGame() {
        try {
            Field field = gameController.getClass().getDeclaredField("game");
            field.setAccessible(true);
            return (Game) field.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field field = gameController.getClass().getDeclaredField("playerViewMap");
            field.setAccessible(true);
            return (Map<String, ViewInterface>) field.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

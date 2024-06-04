package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.controller4.Controller;
import it.polimi.ingsw.controller4.GameController;
import it.polimi.ingsw.controller4.LobbyGameListController;
import it.polimi.ingsw.view.ViewInterface;

import java.lang.reflect.Field;
import java.util.Map;

public class ControllerPublic {
    public Controller controller;

    private String nickname;

    public ControllerPublic(Controller controller) {
        this.controller = controller;
    }

    public String getNickname() {
        try {
            Field field = controller.getClass().getDeclaredField("nickname");
            field.setAccessible(true);
            return (String) field.get(controller);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameController getGameController() {
        try {
            Field field = controller.getClass().getDeclaredField("gameController");
            field.setAccessible(true);
            return (GameController) field.get(controller);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

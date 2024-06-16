package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;

import java.lang.reflect.Field;

public class PublicController {
    public Controller controller;

    public PublicController(Controller controller) {
        this.controller = controller;
    }

    public String getNickname() {
        try {
            Field nicknameField = controller.getClass().getDeclaredField("nickname");
            nicknameField.setAccessible(true);
            return (String) nicknameField.get(controller);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameController getGameController() {
        try {
            Field gameControllerField = controller.getClass().getDeclaredField("gameController");
            gameControllerField.setAccessible(true);
            return (GameController) gameControllerField.get(controller);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

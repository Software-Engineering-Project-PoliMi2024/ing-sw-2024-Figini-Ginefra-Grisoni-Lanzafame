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

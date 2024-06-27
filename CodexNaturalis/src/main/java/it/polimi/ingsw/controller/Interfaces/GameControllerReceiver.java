package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.controller.GameController;

/**
 * This interface is implemented by classes that need to receive the game controller
 * in order to send commands to it
 */
public interface GameControllerReceiver {
    /**
     * This method is used to set the game controller in order to send commands to it
     * @param gameController the game controller to set
     */
    void setGameController(GameController gameController);
}

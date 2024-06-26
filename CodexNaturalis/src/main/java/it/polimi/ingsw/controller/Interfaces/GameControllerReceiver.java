package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.controller.GameController;

public interface GameControllerReceiver {
    /**
     * This method is used to set the game controller in order to send commands to it
     * @param gameController the game controller to set
     */
    void setGameController(GameController gameController);
}

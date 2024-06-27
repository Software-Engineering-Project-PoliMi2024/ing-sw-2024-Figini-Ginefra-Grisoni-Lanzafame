package it.polimi.ingsw.controller.Interfaces;

/**
 * MalevolentPlayerManager is an interface that defines the method to manage a malevolent player
 * when it is detected to perform some not allowed actions
 */
public interface MalevolentPlayerManager {
    /**
     * This method is used to manage a malevolent player when it is
     * detected to perform some not allowed actions
     * It logs the detection in the server and notifies the player
     * @param player is the nickname of the malevolent player
     */
    void manageMalevolentPlayer(String player);
}

package it.polimi.ingsw.view;

/**
 * This interface represents the actual view of the game.
 * It extends the ViewInterface and the ControllerHandler.
 */
public interface ActualView extends ViewInterface, ControllerHandler{
    void run() throws Exception;
}

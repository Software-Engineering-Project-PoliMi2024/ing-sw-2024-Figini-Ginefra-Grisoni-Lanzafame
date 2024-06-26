package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

/**
 * This interface represents an object that provides a controller.
 */
public interface ControllerProvider {
    ControllerInterface getController() throws Exception;
}

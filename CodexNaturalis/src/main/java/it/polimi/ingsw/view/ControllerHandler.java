package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

/**
 * This interface represents an object that provides a controller that can also be set.
 */
public interface ControllerHandler extends ControllerProvider{
    void setController(ControllerInterface controller) throws Exception;
}

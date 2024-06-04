package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

public interface ControllerProvider {
    public ControllerInterface getController() throws Exception;
}

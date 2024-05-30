package it.polimi.ingsw.view;

import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;

public interface ControllerProvider {
    public ControllerInterface getController() throws Exception;
}

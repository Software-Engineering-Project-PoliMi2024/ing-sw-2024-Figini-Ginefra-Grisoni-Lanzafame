package it.polimi.ingsw.view;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.ControllerInterface;

public interface ControllerProvider {
    public ControllerInterface getController() throws Exception;
}

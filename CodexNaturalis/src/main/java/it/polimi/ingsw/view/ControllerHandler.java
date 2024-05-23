package it.polimi.ingsw.view;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.ControllerInterface;

public interface ControllerHandler extends ControllerProvider{
    public void setController(ControllerInterface controller) throws Exception;
}

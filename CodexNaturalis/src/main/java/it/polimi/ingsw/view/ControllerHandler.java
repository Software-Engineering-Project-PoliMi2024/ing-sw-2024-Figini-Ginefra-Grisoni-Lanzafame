package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

public interface ControllerHandler extends ControllerProvider{
    public void setController(ControllerInterface controller) throws Exception;
}

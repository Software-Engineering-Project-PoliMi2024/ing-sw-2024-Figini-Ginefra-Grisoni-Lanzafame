package it.polimi.ingsw.view;

import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;

public interface ControllerHandler extends ControllerProvider{
    public void setController(ControllerInterface controller) throws Exception;
}

package it.polimi.ingsw.view;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;

public interface ControllerProvider {
    public VirtualController getController();
}

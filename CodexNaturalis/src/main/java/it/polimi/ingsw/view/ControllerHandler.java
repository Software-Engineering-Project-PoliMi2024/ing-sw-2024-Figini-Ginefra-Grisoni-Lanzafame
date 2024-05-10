package it.polimi.ingsw.view;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;

public interface ControllerHandler extends ControllerProvider{
    public void setController(VirtualController controller);
}

package it.polimi.ingsw.controller2;

import java.io.Serializable;
import java.rmi.Remote;

public interface ControllerInterfaceClient extends ControllerInterface {
    public void connect(String ip, int port,ViewInterface view);
}

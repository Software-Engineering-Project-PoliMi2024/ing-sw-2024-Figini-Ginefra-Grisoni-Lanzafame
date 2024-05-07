package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.VirtualLayer.VirtualController;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionLayerServer extends Remote, Serializable {
    void connect(PingPongInterface pingPong, ViewInterface view, VirtualController controller) throws Exception;
}

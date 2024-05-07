package it.polimi.ingsw.ConnectionLayer;

import it.polimi.ingsw.ConnectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;

public interface ConnectionLayerServer extends Remote, Serializable {
    void connect(PingPongInterface pingPong, ViewInterface view, VirtualController controller) throws Exception;
}

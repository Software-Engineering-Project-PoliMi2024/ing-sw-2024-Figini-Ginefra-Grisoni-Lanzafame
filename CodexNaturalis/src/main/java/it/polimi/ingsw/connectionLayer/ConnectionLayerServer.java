package it.polimi.ingsw.connectionLayer;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;

public interface ConnectionLayerServer extends Remote, Serializable {
    void connect(PingPongInterface pingPong, ViewInterface view, VirtualController controller) throws Exception;
}

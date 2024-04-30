package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;

public interface ConnectionLayerClient extends Remote, Serializable {
    void connect(String ip, int port, ViewInterface view);
}
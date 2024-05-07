package it.polimi.ingsw.ConnectionLayer;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionLayerClient extends Remote, Serializable {
    void connect(String ip, int port, ViewInterface view) throws Exception;
}
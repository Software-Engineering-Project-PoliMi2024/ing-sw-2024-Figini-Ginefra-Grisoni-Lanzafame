package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionLayerClient extends Remote, Serializable {
    public void connect(String ip, int port, ViewInterface view) throws RemoteException;

}
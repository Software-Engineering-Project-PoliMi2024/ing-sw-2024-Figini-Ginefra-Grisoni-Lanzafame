package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionLayerServer extends Remote {
    void connect(ViewInterface view) throws RemoteException;
}

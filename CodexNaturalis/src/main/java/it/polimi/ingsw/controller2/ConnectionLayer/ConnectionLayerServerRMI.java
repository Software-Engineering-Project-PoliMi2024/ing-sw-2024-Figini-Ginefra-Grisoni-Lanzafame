package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionLayerServerRMI extends ConnectionLayerServer, Remote, Serializable {
    void setConnectionClient(ConnectionLayerClient client) throws RemoteException;
}

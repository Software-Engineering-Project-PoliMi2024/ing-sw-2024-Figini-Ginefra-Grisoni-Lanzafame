package it.polimi.ingsw.controller2.pingPong;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PingPongInterface extends Remote, Serializable {
    void pingPong() throws RemoteException;
}

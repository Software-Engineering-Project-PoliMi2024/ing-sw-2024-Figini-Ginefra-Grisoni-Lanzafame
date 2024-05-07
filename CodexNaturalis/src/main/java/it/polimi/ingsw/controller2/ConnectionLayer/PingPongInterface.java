package it.polimi.ingsw.controller2.ConnectionLayer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PingPongInterface extends Remote, Serializable {
    void pingPong() throws Exception;
    void setPingPongStub(PingPongInterface pingPongStub) throws Exception;
    void checkEmpty() throws Exception;
}

package it.polimi.ingsw.ConnectionLayer;

import java.io.Serializable;
import java.rmi.Remote;

public interface PingPongInterface extends Remote, Serializable {
    void pingPong() throws Exception;
    void setPingPongStub(PingPongInterface pingPongStub) throws Exception;
    void checkEmpty() throws Exception;
}

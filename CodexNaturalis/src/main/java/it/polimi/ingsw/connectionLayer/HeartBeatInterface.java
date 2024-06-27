package it.polimi.ingsw.connectionLayer;

import java.io.Serializable;
import java.rmi.Remote;

public interface HeartBeatInterface extends Remote, Serializable {
    void heartBeat() throws Exception;
    void setHeartBeatStub(HeartBeatInterface heartBeatStub) throws Exception;
    void checkEmpty() throws Exception;
}

package it.polimi.ingsw.controller2;

import java.rmi.Remote;

public interface ConnectionLayerClient extends Remote {
    public void connect(String ip, int port, ViewInterface view);
}
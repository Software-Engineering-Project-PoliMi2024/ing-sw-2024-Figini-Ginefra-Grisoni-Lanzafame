package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.LogsFromServer;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionClientRMI implements ConnectionLayerClient{

    public void connect(String ip, int port, ViewInterface view) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ConnectionServerRMI connect = (ConnectionServerRMI) registry.lookup("connect");
            connect.connect(view);
        } catch (Exception e) {
            e.printStackTrace();
            view.log(LogsFromServer.CONNECTION_ERROR.getMessage());
        }
    }
}

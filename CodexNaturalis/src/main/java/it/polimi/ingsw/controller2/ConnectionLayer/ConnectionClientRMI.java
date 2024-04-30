package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.LogsFromServer;
import it.polimi.ingsw.view.ViewInterface;

import java.nio.channels.ScatteringByteChannel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ConnectionClientRMI implements ConnectionLayerClient{

    public void connect(String ip, int port, ViewInterface view) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ConnectionLayerServer connect = null;
            try {
                connect = (ConnectionLayerServer) registry.lookup("connect");
            }catch (NotBoundException b){
                b.printStackTrace();
            }
            ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
            connect.connect(viewStub);
        } catch (RemoteException r) {
            r.printStackTrace();
            try {
                view.log(LogsFromServer.CONNECTION_ERROR.getMessage());
            }catch (RemoteException rr) {
                rr.printStackTrace();
            }
        }
    }
}

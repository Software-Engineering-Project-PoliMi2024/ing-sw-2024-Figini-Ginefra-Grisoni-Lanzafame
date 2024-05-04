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
    /**
     * Establishes a connection with the RMI server located at the specified IP address and port.
     * This method enables communication between a client and the server through RMI.
     * @param ip The IP address of the RMI server.
     * @param port The port number where the RMI server is listening.
     * @param view the view of the client
     */
    public void connect(String ip, int port, ViewInterface view) {
        //TODO: if the ip or port are wrong, notify the user, use Futures
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ip, port);
            System.out.println(registry.toString());
            ConnectionLayerServer connect;
            connect = (ConnectionLayerServer) registry.lookup("connect");
            System.out.println(connect.toString());
            ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
            connect.connect(viewStub);
        }catch (Exception e){
            try {
                view.logErr(LogsFromServer.CONNECTION_ERROR.getMessage());
            }catch (RemoteException remoteException){
                remoteException.printStackTrace();
            }
        }
    }
}

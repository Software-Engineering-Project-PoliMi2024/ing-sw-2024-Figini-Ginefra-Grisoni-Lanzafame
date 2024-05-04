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
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ConnectionLayerServer connect = null;
            try {
                connect = (ConnectionLayerServer) registry.lookup("connect");
            }catch (NotBoundException b){
                System.out.println("No method connect found on the Server");
                view.log(LogsFromServer.CONNECTION_ERROR.getMessage());
                b.printStackTrace();
            }
            //expose client's view to the server
            ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
            connect.connect(viewStub);
        } catch (RemoteException r) { //serverOfflient
            r.printStackTrace();
            try {
                view.log(LogsFromServer.CONNECTION_ERROR.getMessage());
            }catch (RemoteException rr) {
                rr.printStackTrace();
            }
        }
    }
}

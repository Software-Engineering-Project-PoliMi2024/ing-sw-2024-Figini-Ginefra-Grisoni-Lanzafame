package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.SignificantPaths;
import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectionClientRMI implements ConnectionLayerClient{
    private final ExecutorService clientExecutor = Executors.newSingleThreadExecutor();
    private ViewInterface view;
    int secondsTimeOut = SignificantPaths.secondsTimeOut;

    /**
     * Establishes a connection with the RMI server located at the specified IP address and port.
     * This method enables communication between a client and the server through RMI.
     * @param ip The IP address of the RMI server.
     * @param port The port number where the RMI server is listening.
     * @param view the view of the client
     */
    public void connect(String ip, int port, ViewInterface view) throws RemoteException{
        Future<ConnectionLayerServer> connect = clientExecutor.submit(() -> {
            try {
                Registry registry = LocateRegistry.getRegistry(ip, port);
                ConnectionLayerServer serverReference = (ConnectionLayerServer) registry.lookup("connect");
                ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
                serverReference.connect(viewStub);
                return serverReference;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        try {
            ConnectionLayerServer serverStub = connect.get(secondsTimeOut, TimeUnit.SECONDS);
            if(serverStub == null)
                throw new NullPointerException();
        }
        catch (Exception e){
            try {
                //e.printStackTrace();
                view.logErr(LogsOnClient.CONNECTION_ERROR.getMessage());
            }catch (RemoteException r){
            }
        }
    }
}

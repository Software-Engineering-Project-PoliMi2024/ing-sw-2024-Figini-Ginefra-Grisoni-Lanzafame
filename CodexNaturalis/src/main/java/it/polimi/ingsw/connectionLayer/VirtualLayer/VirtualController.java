package it.polimi.ingsw.connectionLayer.VirtualLayer;

import it.polimi.ingsw.connectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.controller2.ControllerInterface;

import java.rmi.RemoteException;

public interface VirtualController extends ControllerInterface, ConnectionLayerClient, PingPongInterface {
    @Override
    void checkEmpty() throws RemoteException;
    @Override
    void setPingPongStub(PingPongInterface pingPongStub) throws RemoteException;
    void setControllerStub(ControllerInterface controllerStub) throws RemoteException;
}

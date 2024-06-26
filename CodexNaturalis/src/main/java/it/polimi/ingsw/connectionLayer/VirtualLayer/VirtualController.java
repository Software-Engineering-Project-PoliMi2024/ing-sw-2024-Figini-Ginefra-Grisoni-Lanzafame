package it.polimi.ingsw.connectionLayer.VirtualLayer;

import it.polimi.ingsw.connectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

import java.rmi.RemoteException;

public interface VirtualController extends ControllerInterface, ConnectionLayerClient, PingPongInterface {
        @Override
    void checkEmpty() throws Exception;
    @Override
    void setPingPongStub(PingPongInterface pingPongStub) throws Exception;
    void setControllerStub(ControllerInterface controllerStub) throws Exception;
    void disconnect() throws Exception;
}

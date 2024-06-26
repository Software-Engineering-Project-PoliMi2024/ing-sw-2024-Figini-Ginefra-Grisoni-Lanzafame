package it.polimi.ingsw.connectionLayer.VirtualLayer;

import it.polimi.ingsw.connectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.connectionLayer.HeartBeatInterface;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

public interface VirtualController extends ControllerInterface, ConnectionLayerClient, HeartBeatInterface {
        @Override
    void checkEmpty() throws Exception;
    @Override
    void setHeartBeatStub(HeartBeatInterface heartBeatStub) throws Exception;
    void setControllerStub(ControllerInterface controllerStub) throws Exception;
    void disconnect() throws Exception;
}

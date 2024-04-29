package it.polimi.ingsw.controller2;

public interface ConnectionLayerClient extends ControllerInterface {
    public void connect(String ip, int port, ViewInterface view);
}

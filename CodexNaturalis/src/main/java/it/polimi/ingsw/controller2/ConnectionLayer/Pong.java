package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.ServerModelController;

import java.rmi.RemoteException;

public class Pong implements PingPongInterface{
    private final ControllerInterface controller;
    private Ping ping;

    public Pong(ControllerInterface controller){
        this.controller = controller;
    }
    public void pingPong() throws RemoteException {
        try {
            ping.pingPong();
        } catch (RemoteException e) {
            controller.disconnect();
        }
    }

    public void setPing(Ping ping) {
        this.ping = ping;
    }
}

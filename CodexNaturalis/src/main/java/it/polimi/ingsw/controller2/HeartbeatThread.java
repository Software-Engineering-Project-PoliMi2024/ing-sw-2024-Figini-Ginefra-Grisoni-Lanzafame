package it.polimi.ingsw.controller2;

import it.polimi.ingsw.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class HeartbeatThread extends Thread{
    private ServerModelController controller;
    private ViewInterface view;

    private Boolean stop = false;

    public HeartbeatThread(ServerModelController controller, ViewInterface view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                // Sleep for 3 seconds
                Thread.sleep(3000);
                // Check if client is still active
                try{
                    view.isClientOn();
                }catch (RemoteException r){
                    controller.receiveHeartbeat(false);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }
}

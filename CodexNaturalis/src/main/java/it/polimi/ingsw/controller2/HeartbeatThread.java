package it.polimi.ingsw.controller2;

import it.polimi.ingsw.view.ViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class HeartbeatThread extends Thread{
    private final ServerModelController controller;
    private final ViewInterface view;

    private Boolean stop = false;

    /**
     * The constructor of the class
     * @param controller of the client which is being pinged
     * @param view of the client which is being pinged
     */
    public HeartbeatThread(ServerModelController controller, ViewInterface view) {
        this.controller = controller;
        this.view = view;
    }

    /**
     * Ping the client every 3 seconds to see if it is still online
     */
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

    /**
     * @param stop if set to true kill the thread
     */
    public void setStop(Boolean stop) {
        this.stop = stop;
    }
}

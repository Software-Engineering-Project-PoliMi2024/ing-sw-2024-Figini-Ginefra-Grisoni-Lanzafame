package it.polimi.ingsw.controller.RMI;

import it.polimi.ingsw.controller.ClientImplementation;
import it.polimi.ingsw.view.View;

import java.rmi.registry.Registry;

public class ClientRMI extends ClientImplementation {
    private final Registry registry;
    /**@param nickname the nickname used by the user
     * @param view the view of user choice (TUI / GUI)
     * @param registry the API registry for RMI*/
    public ClientRMI(String nickname, View view, Registry registry) {
        super(nickname, view);
        this.registry = registry;
    }

    public void run(){


        while(!shallTerminate)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

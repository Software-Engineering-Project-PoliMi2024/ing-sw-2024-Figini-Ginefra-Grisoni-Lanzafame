package it.polimi.ingsw.controller.RMI;

import it.polimi.ingsw.controller.RMI.remoteInterfaces.loginRMI;
import it.polimi.ingsw.controller.RMI.remoteInterfaces.multiGames;
import it.polimi.ingsw.controller.clientImplementation;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.view.View;

import java.rmi.registry.Registry;
import java.util.Scanner;

public class clientRMI extends clientImplementation{
    private final Registry registry;
    /**@param nickname the nickname used by the user
     * @param view the view of user choice (TUI / GUI)
     * @param registry the API registry for RMI*/
    public clientRMI(String nickname, View view, Registry registry) {
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

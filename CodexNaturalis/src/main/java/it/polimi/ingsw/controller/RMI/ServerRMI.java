package it.polimi.ingsw.controller.RMI;

import it.polimi.ingsw.controller.ServerImplementation;
import it.polimi.ingsw.model.MultiGame;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.controller.RMI.remoteInterfaces.LoginRMI;

public class ServerRMI extends ServerImplementation {
    /**@implNote the port must not be a constant*/
    private final int port = 4445;
    LoginRMI loginRMI = new LoginRMI();
    public ServerRMI(MultiGame games) {
        super(games);
    }
    public void run() {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(LabelAPI.Login.getLabel(), loginRMI);

            System.out.println("RMI Server started on port " + port + "🚔!");
        } catch (Exception e) {
            System.err.println("Server exception: can't open registry " +
                    "or error while binding the object");
            e.printStackTrace();
        }
    }

}

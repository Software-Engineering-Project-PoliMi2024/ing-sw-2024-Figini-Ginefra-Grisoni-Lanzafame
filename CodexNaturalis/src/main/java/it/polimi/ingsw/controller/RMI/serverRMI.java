package it.polimi.ingsw.controller.RMI;

import it.polimi.ingsw.controller.RMI.remoteInterfaces.multiGames;
import it.polimi.ingsw.controller.serverImplementation;
import it.polimi.ingsw.model.MultiGame;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.controller.RMI.remoteInterfaces.loginRMI;

public class serverRMI extends serverImplementation{
    /**@implNote the port must not be a constant*/
    private final int port = 4445;
    loginRMI loginRMI = new loginRMI();
    multiGames multiGames = new multiGames(games);
    public serverRMI(MultiGame games) {
        super(games);
    }
    public void run() {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(labelAPI.Login.getLabel(), loginRMI);
            registry.bind(labelAPI.GetMultiGames.getLabel(), multiGames);

            System.out.println("RMI Server started on port " + port + "🚔!");
        } catch (Exception e) {
            System.err.println("Server exception: can't open registry " +
                    "or error while binding the object");
            e.printStackTrace();
        }
    }

}

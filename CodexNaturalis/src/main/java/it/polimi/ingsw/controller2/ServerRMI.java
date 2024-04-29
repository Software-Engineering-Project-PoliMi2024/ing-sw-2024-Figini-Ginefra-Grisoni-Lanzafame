package it.polimi.ingsw.controller2;

import it.polimi.ingsw.controller.RMI.LabelAPI;
import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.view.ViewState;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ServerRMI extends ServerImplementation {
    Registry registry;

    public ServerRMI(ServerModelController serverModelController) {
        super(serverModelController, 1234);
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void run() {
        try {
            this.setRegistry(LocateRegistry.createRegistry(this.getPort()));


            System.out.println("RMI Server started on port " + getPort() + "🚔!");
        } catch (Exception e) {
            System.err.println("Server exception: can't open registry " +
                    "or error while binding the object");
            e.printStackTrace();
        }
    }
    @Override
    public void connect(String ip, int port) {

    }
}

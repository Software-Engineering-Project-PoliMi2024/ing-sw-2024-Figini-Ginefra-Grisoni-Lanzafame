package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionClientRMI {

    public void connect(String ip, int port,ViewInterface view) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ConnectionServerRMI connect = (ConnectionServerRMI) registry.lookup("connect");
            connect.connect(view);
        } catch (Exception e) {
            e.printStackTrace();
            view.log(LogsFromServer.CONNECTION_ERROR.getMessage());
        }
    }
}

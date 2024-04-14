package it.polimi.ingsw.controller.RMI.remoteInterfaces;

import it.polimi.ingsw.model.MultiGame;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class multiGames implements Remote, Serializable {
    private final MultiGame games;
    public multiGames(MultiGame games){
        this.games = games;
    }

    public MultiGame getMultiGame() throws RemoteException {
        return games;
    }
}

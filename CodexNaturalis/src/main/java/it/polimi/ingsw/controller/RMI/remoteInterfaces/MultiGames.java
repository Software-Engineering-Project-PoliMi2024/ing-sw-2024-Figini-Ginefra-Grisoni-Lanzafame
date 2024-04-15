package it.polimi.ingsw.controller.RMI.remoteInterfaces;

import it.polimi.ingsw.model.MultiGame;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class MultiGames implements Remote, Serializable {
    private final MultiGame games;
    public MultiGames(MultiGame games){
        this.games = games;
    }

    public MultiGame getMultiGame() throws RemoteException {
        return games;
    }
}

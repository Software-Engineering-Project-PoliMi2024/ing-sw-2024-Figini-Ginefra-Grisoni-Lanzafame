package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract class ConnectionLayerClient implements Remote, Serializable {
    private ViewInterface view;
    public void connect(String ip, int port, ViewInterface view){}
    public void ping() throws RemoteException{}
    public void erase() throws RemoteException{
        view.updateLobbyList(new FatManLobbyList());
        view.updateLobby(new LittleBoyLobby());
        view.updateGame(new GadgetGame());
    }
}
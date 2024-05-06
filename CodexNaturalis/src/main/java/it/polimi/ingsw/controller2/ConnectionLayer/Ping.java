package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;

public class Ping implements PingPongInterface {
    private final ViewInterface view;
    private Pong pong;

    public Ping(ViewInterface view){
        this.view = view;
    }
    public void pingPong() throws RemoteException {
        try {
            pong.pingPong();
        } catch (RemoteException e) {
            eraseView();
            disconnect();
        }
    }

    public void setPong(Pong pong) {
        this.pong = pong;
    }

    private void eraseView() {
        try {
            view.updateLobbyList(new FatManLobbyList());
            view.updateLobby(new LittleBoyLobby());
            view.updateGame(new GadgetGame());
        }catch (RemoteException r){
        }
    }
    private void disconnect() {
        try {
            view.logErr(LogsOnClient.CONNECTION_LOST_CLIENT_SIDE.getMessage());
            view.transitionTo(ViewState.SERVER_CONNECTION);
        }catch (RemoteException r){
        }
    }
}

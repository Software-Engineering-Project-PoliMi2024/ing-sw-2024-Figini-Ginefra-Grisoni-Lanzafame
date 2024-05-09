package it.polimi.ingsw.connectionLayer.VirtualSocket;

import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.LogMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.TransitionToMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;

public class VirtualViewSocket implements VirtualView {
    private ControllerInterface controller;
    private final ClientHandler clientHandler;

    public VirtualViewSocket(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }
    @Override
    public void checkEmpty() throws RemoteException {

    }

    @Override
    public void setPingPongStub(PingPongInterface pingPongStub) throws RemoteException {

    }

    @Override
    public void setController(ControllerInterface controller) throws RemoteException {
        this.controller = controller;
    }

    @Override
    public void pingPong() throws RemoteException {

    }

    @Override
    public void setState(ViewState state) throws RemoteException {

    }

    @Override
    public void transitionTo(ViewState state) throws RemoteException {
        clientHandler.sendServerMessage(new TransitionToMsg(state));
    }

    @Override
    public void log(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogMsg(logMsg));
    }

    @Override
    public void logErr(String logMsg) throws RemoteException {

    }

    @Override
    public void logOthers(String logMsg) throws RemoteException {

    }

    @Override
    public void logGame(String logMsg) throws RemoteException {

    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {

    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {

    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {

    }

    @Override
    public void setFinalRanking(String[] nicks, int[] points) throws RemoteException {

    }

    public ControllerInterface getController() {
        return controller;
    }
}

package it.polimi.ingsw.connectionLayer.VirtualSocket;

import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.*;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

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
    public void transitionTo(ViewState state) throws RemoteException {
        clientHandler.sendServerMessage(new TransitionToMsg(state));
    }

    @Override
    public void log(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogMsg(logMsg));
    }

    @Override
    public void logErr(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogErrMsg(logMsg));
    }

    @Override
    public void logOthers(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogOthersMsg(logMsg));
    }

    @Override
    public void logGame(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogGameMsg(logMsg));
    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        clientHandler.sendServerMessage(new UpdateLobbyListMsg(diff));
    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        clientHandler.sendServerMessage(new UpdateLobbyMsg(diff));
    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        clientHandler.sendServerMessage(new UpdateGameMsg(diff));
    }

    @Override
    public void setFinalRanking(List<String> ranking) throws RemoteException {
        clientHandler.sendServerMessage(new SetFinalRankingMsg(ranking));
    }

    public ControllerInterface getController() {
        return controller;
    }
}

package it.polimi.ingsw.connectionLayer.VirtualLayer;

import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualView extends ViewInterface, PingPongInterface {
    @Override
    void checkEmpty() throws RemoteException;
    @Override
    void setPingPongStub(PingPongInterface pingPongStub) throws RemoteException;
    void setController(ControllerInterface controller) throws RemoteException;
    @Override
    public void pingPong() throws RemoteException;

    @Override
    public void transitionTo(ViewState state) throws RemoteException;

    @Override
    public void log(String logMsg) throws RemoteException;

    @Override
    public void logErr(String logMsg) throws RemoteException;

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException;

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException;

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException;

    @Override
    public void setFinalRanking(List<String> ranking) throws RemoteException;
}

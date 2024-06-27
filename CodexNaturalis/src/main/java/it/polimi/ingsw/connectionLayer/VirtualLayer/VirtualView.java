package it.polimi.ingsw.connectionLayer.VirtualLayer;

import it.polimi.ingsw.connectionLayer.HeartBeatInterface;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.util.List;

public interface VirtualView extends ViewInterface, HeartBeatInterface {
    @Override
    void checkEmpty() throws RemoteException;
    @Override
    void setHeartBeatStub(HeartBeatInterface heartBeatStub) throws RemoteException;
    void setController(ControllerInterface controller) throws RemoteException;
    @Override
    public void heartBeat() throws RemoteException;

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
}

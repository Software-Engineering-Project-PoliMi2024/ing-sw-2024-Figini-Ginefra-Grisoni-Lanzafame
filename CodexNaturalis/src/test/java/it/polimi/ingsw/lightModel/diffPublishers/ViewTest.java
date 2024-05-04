package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;

public class ViewTest implements ViewInterface {
    public String name;
    public LightLobbyList lightLobbyList;
    public LightLobby lightLobby;
    public LightGame lightGame;

    public ViewTest() {
        lightLobbyList = new LightLobbyList();
        lightLobby = new LightLobby();
        lightGame = new LightGame();
    }

    @Override
    public void isClientOn() {

    }

    @Override
    public void setState(ViewState state) throws RemoteException {

    }

    @Override
    public void transitionTo(ViewState state) throws RemoteException {

    }

    @Override
    public void postConnectionInitialization(ControllerInterface controller) throws RemoteException {

    }

    @Override
    public void log(String logMsg) throws RemoteException {

    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        diff.apply(this.lightLobbyList);
        System.out.println("lobbyList updated");
    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        diff.apply(this.lightLobby);
    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        diff.apply(this.lightGame);
    }

    @Override
    public void setFinalRanking(String[] nicks, int[] points) throws RemoteException {

    }

}

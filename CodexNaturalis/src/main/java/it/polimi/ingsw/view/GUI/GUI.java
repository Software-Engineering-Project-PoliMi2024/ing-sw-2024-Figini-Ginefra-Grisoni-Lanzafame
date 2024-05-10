package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewState;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.util.Arrays;

public class GUI extends View {
    private final ApplicationGUI applicationGUI;
    public GUI(VirtualController controller){
        super(controller);
        ControllerGUI.setController(controller);
        ControllerGUI.setView(this);
        this.applicationGUI = new ApplicationGUI();
        ControllerGUI.setApplicationGUI(applicationGUI);
    }

    @Override
    public void log(String logMsg) throws RemoteException {

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

    @Override
    public void run() {
        Application.launch(applicationGUI.getClass());
    }

    @Override
    public void transitionTo(ViewState state){
        System.out.println("Transitioning to " + state);
        StateGUI stateGUI = Arrays.stream(StateGUI.values()).filter(s -> s.references(state)).findFirst().orElseThrow();
        System.out.println("Transitioning to " + stateGUI);
    }
}

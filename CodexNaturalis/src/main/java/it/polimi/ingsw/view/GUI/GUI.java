package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.GUI.Controllers.ConnectionFormControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LoginFormControllerGUI;
import it.polimi.ingsw.view.ViewState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Arrays;

public class GUI extends Application implements ActualView {
    private Stage primaryStage;
    static private VirtualController controller;

    private Root currentRoot;

    public void run() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        ConnectionFormControllerGUI.view = this;
        LoginFormControllerGUI.view = this;
        this.primaryStage = primaryStage;
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Codex In Naturalis");
        primaryStage.setScene(new Scene(Root.SERVER_CONNECTION_FORM.getRoot(), 800, 600));
        transitionTo(StateGUI.SERVER_CONNECTION);
    }

    private void setRoot(Root root){
        currentRoot = root;
        primaryStage.getScene().setRoot(root.getRoot());
        primaryStage.show();
    }

    public void transitionTo(StateGUI state) {
        Platform.runLater(() -> {
            if(!state.getRoot().equals(currentRoot))
                setRoot(state.getRoot());
        });
    }


    @Override
    public void transitionTo(ViewState state) throws RemoteException {
        StateGUI newState = Arrays.stream(StateGUI.values())
                .filter(s -> s.references(state))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid state: " + state));
        transitionTo(newState);
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
    public void setController(VirtualController controller) {
        GUI.controller = controller;
    }

    @Override
    public VirtualController getController() {
        return controller;
    }

    public static VirtualController getControllerStatic() {
        return controller;
    }

    public static void setControllerStatic(VirtualController controller) {
        GUI.controller = controller;
    }


}

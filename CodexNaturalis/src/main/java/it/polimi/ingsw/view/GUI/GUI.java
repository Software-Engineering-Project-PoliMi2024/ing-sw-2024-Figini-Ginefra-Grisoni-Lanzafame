package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.game.HandDiffAdd;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.GUI.Controllers.ConnectionFormControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LobbyListControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LoginFormControllerGUI;
import it.polimi.ingsw.view.ViewState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Arrays;

public class GUI extends Application implements ActualView {
    static private VirtualController controller;
    private static final LightLobbyList lobbyList = new LightLobbyList();

    private static LightGame lightGame = new LightGame();

    private Stage primaryStage;
    private Root currentRoot;

    public void run() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        ModelDiffs<LightGame> diff = new HandDiffAdd(new LightCard(1), true);
        diff.apply(lightGame);

        diff = new HandDiffAdd(new LightCard(2), true);
        diff.apply(lightGame);

        diff = new HandDiffAdd(new LightCard(3), true);
        diff.apply(lightGame);


        ConnectionFormControllerGUI.view = this;

        LoginFormControllerGUI.setView(this);

        this.primaryStage = primaryStage;
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Codex In Naturalis");
        primaryStage.setScene(new Scene(Root.SERVER_CONNECTION_FORM.getRoot(), 800, 600));
        transitionTo(StateGUI.SERVER_CONNECTION);
        //transitionTo(StateGUI.IDLE);
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
        diff.apply(lobbyList);
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

    public static LightGame getLightGame(){
        return lightGame;
    }

    public static LightLobbyList getLobbyList(){
        return lobbyList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

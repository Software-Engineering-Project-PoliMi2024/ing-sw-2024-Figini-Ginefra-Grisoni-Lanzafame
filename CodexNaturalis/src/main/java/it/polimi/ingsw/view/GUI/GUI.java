package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.lightModel.LogMemory;
import it.polimi.ingsw.view.GUI.Components.LogErr;
import it.polimi.ingsw.view.GUI.Components.logoSwapAnimation;
import it.polimi.ingsw.view.GUI.Controllers.ConnectionFormControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LoginFormControllerGUI;
import it.polimi.ingsw.view.ViewState;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.Arrays;

public class GUI extends Application implements ActualView {
    static private VirtualController controller;
    private static final LightLobbyList lobbyList = new LightLobbyList();
    private static final LightLobby lobby = new LightLobby();
    private static final LightGame lightGame = new LightGame();

    private static LogMemory logMemory = new LogMemory();

    private Stage primaryStage;
    private Root currentRoot;
    private final AnchorPane stackRoot = new AnchorPane();

    private logoSwapAnimation transitionAnimation;

    public void run() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        transitionAnimation = new logoSwapAnimation(primaryStage);


        ConnectionFormControllerGUI.view = this;

        LoginFormControllerGUI.setView(this);

        this.primaryStage = primaryStage;
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Codex In Naturalis");
        primaryStage.getIcons().add(CardMuseumGUI.logo);
        primaryStage.setScene(new Scene(stackRoot, 800, 600));

        AnchorPane.setTopAnchor(stackRoot, 0.0);
        AnchorPane.setBottomAnchor(stackRoot, 0.0);
        AnchorPane.setLeftAnchor(stackRoot, 0.0);
        AnchorPane.setRightAnchor(stackRoot, 0.0);

        primaryStage.show();

        //set stackRoot background and style
        //stackRoot.setStyle("-fx-background-color: #1e1f22;");
        //transitionTo(StateGUI.SERVER_CONNECTION);
        //transitionTo(StateGUI.JOIN_LOBBY);
        //transitionTo(StateGUI.LOBBY);
        transitionTo(StateGUI.IDLE);
    }

    private void setRoot(Root root){
        if(currentRoot == null){
            stackRoot.getChildren().add(root.getRoot());
            currentRoot = root;
            primaryStage.show();
            return;
        }

        transitionAnimation.playTransitionAnimation(stackRoot, currentRoot, root);
        currentRoot = root;

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
        Platform.runLater(()->LogErr.display(logMsg));
    }

    @Override
    public void logOthers(String logMsg) throws RemoteException {
        Platform.runLater(()->logMemory.addLog(logMsg));
    }

    @Override
    public void logGame(String logMsg) throws RemoteException {
    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        Platform.runLater(() -> diff.apply(lobbyList));
    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        Platform.runLater(() -> diff.apply(lobby));
    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        Platform.runLater(() -> diff.apply(lightGame));
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

    public static LightLobby getLobby(){
        return lobby;
    }

    public static LightLobbyList getLobbyList(){
        return lobbyList;
    }

    public static LogMemory getLogMemory() {
        return logMemory;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

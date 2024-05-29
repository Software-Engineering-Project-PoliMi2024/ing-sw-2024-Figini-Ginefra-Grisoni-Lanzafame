package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.lightModel.LogMemory;
import it.polimi.ingsw.view.GUI.Components.Logs.LogErr;
import it.polimi.ingsw.view.GUI.Components.Utils.EnumProperty;
import it.polimi.ingsw.view.GUI.Components.Utils.logoSwapAnimation;
import it.polimi.ingsw.view.GUI.Controllers.ConnectionFormControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LoginFormControllerGUI;
import it.polimi.ingsw.view.ViewState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUI extends Application implements ActualView {
    static private ControllerInterface controller;
    private static final LightLobbyList lobbyList = new LightLobbyList();
    private static final LightLobby lobby = new LightLobby();
    private static final LightGame lightGame = new LightGame();

    private static LogMemory logMemory = new LogMemory();

    private Stage primaryStage;
    private Node currentRoot;
    private final AnchorPane stackRoot = new AnchorPane();

    private logoSwapAnimation transitionAnimation;

    private static final EnumProperty<StateGUI> stateProperty = new EnumProperty<>();

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
        primaryStage.getIcons().add(AssetsGUI.logo);
        primaryStage.setScene(new Scene(stackRoot, 800, 600));

        AnchorPane.setTopAnchor(stackRoot, 0.0);
        AnchorPane.setBottomAnchor(stackRoot, 0.0);
        AnchorPane.setLeftAnchor(stackRoot, 0.0);
        AnchorPane.setRightAnchor(stackRoot, 0.0);

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try {
                controller.disconnect();
            }catch (Exception e){}
        });

            primaryStage.show();

        //set stackRoot background and style
        //stackRoot.setStyle("-fx-background-color: #1e1f22;");
        transitionTo(StateGUI.SERVER_CONNECTION);
        //transitionTo(StateGUI.SELECT_OBJECTIVE);
        //transitionTo(StateGUI.JOIN_LOBBY);
        //transitionTo(StateGUI.LOBBY);
        //transitionTo(StateGUI.PLACE_CARD);
    }

    private void setRoot(Node root){
        if(currentRoot == null){
            stackRoot.getChildren().add(root);
            currentRoot = root;
            primaryStage.show();
            return;
        }

        transitionAnimation.playTransitionAnimation(stackRoot, currentRoot, root);
        currentRoot = root;

    }

    public void transitionTo(StateGUI state) {
        if(!state.equals(stateProperty.get())) {
            Platform.runLater(() -> {
                setRoot(state.getScene().getContent());
            });
        }
        stateProperty.set(state);
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
    public void setFinalRanking(List<String> ranking) throws RemoteException {
            Platform.runLater(() -> transitionTo(StateGUI.GAME_ENDING));
    }

    @Override
    public void setController(ControllerInterface controller) {
        GUI.controller = controller;
    }

    @Override
    public ControllerInterface getController() {
        return controller;
    }

    public static ControllerInterface getControllerStatic() {
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

    public static EnumProperty<StateGUI> getStateProperty() {
        return stateProperty;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

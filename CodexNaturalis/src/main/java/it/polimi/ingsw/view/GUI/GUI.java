package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.lightModel.LogMemory;
import it.polimi.ingsw.view.GUI.Components.LeaderboardGUI;
import it.polimi.ingsw.view.GUI.Components.Logs.LogErr;
import it.polimi.ingsw.view.GUI.Components.Utils.EnumProperty;
import it.polimi.ingsw.view.GUI.Components.Utils.logoSwapAnimation;
import it.polimi.ingsw.view.GUI.Controllers.ConnectionFormControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LoginFormControllerGUI;
import it.polimi.ingsw.view.ViewState;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The main class of the GUI. It manages the transitions between the different scenes and holds the controller.
 * It also manages the logs and the updates of the lobby, the game and the lobby list.

 */
public class GUI extends Application implements ActualView {
    /** The controller */
    static private ControllerInterface controller;

    //==================================================================================================================
    // LIGHT MODEL
    //==================================================================================================================
    /** The lobby list */
    private static final LightLobbyList lobbyList = new LightLobbyList();
    /** The lobby */
    private static final LightLobby lobby = new LightLobby();
    /** The game */
    private static final LightGame lightGame = new LightGame();
    //==================================================================================================================

    /**The holder of the logs*/
    private static final LogMemory logMemory = new LogMemory();

    /** The one and only stage */
    private Stage primaryStage;

    /** The current root containing the current scene */
    private Node currentRoot;

    /** The stack root containing all the scenes. This is useful for the transition animations */
    private final AnchorPane stackRoot = new AnchorPane();

    /** The transition animation */
    private logoSwapAnimation transitionAnimation;

    /** The state of the GUI */
    private static final EnumProperty<StateGUI> stateProperty = new EnumProperty<>();

    /** The method that runs the GUI */
    public void run() {
        launch();
    }

    /** The method that starts the GUI */
    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/GUI/Styles/themes/cupertino-light.css")).toExternalForm());
        transitionAnimation = new logoSwapAnimation(primaryStage);

        ConnectionFormControllerGUI.view = this;
        LoginFormControllerGUI.setView(this);

        this.primaryStage = primaryStage;
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Codex In Naturalis");
        primaryStage.getIcons().add(AssetsGUI.logo);

        Scene primaryScene = new Scene(stackRoot, 800, 600);
        primaryScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/global.css")).toExternalForm());
        primaryStage.setScene(primaryScene);


        AnchorPane.setTopAnchor(stackRoot, 0.0);
        AnchorPane.setBottomAnchor(stackRoot, 0.0);
        AnchorPane.setLeftAnchor(stackRoot, 0.0);
        AnchorPane.setRightAnchor(stackRoot, 0.0);

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try{
                controller.leave();
                System.exit(0);
            }catch (Exception e){}
        });

            primaryStage.show();

        transitionTo(StateGUI.SERVER_CONNECTION);
    }

    /** The method that sets the root of the scene. This triggers the transition animation */
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

    /** The method that transitions to a new state of the GUI by setting the respective root */
    public void transitionTo(StateGUI state) {
        if(!state.equals(stateProperty.get())) {
            Platform.runLater(() -> {
                setRoot(state.getScene().getContent());
            });
        }
        stateProperty.set(state);
    }

    /** The method that transitions to a new state */
    @Override
    public void transitionTo(ViewState state) throws RemoteException {
        StateGUI newState = Arrays.stream(StateGUI.values())
                .filter(s -> s.references(state))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid state: " + state));
        transitionTo(newState);
    }

    /** The method that logs a message */
    @Override
    public void log(String logMsg) throws RemoteException {
    }

    /** The method that logs an error */
    @Override
    public void logErr(String logMsg) throws RemoteException {
        Platform.runLater(() -> LogErr.display(stackRoot, logMsg));
    }

    /*+ The method that logs a message that is not a log or an error*/
    @Override
    public void logOthers(String logMsg) throws RemoteException {
        Platform.runLater(()->logMemory.addLog(logMsg));
    }

    /** The method that logs a game message */
    @Override
    public void logGame(String logMsg) throws RemoteException {
    }

    /** The method that logs a chat message */
    @Override
    public void logChat(String logMsg) throws RemoteException {
    }

    /** The method that applies the given diff to the lobby list
     * @param diff the diff to apply
     */
    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        Platform.runLater(() -> diff.apply(lobbyList));
    }

    /** The method that applies the given diff to the lobby
     * @param diff the diff to apply
     */
    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        Platform.runLater(() -> diff.apply(lobby));
    }

    /** The method that applies the given diff to the game
     * @param diff the diff to apply
     */
    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        Platform.runLater(() -> diff.apply(lightGame));
    }

    /** Sets the controller
     * @param controller the controller to set
     */
    @Override
    public void setController(ControllerInterface controller) {
        GUI.controller = controller;
    }

    /**
     * Getter of the controller
     * @return the controller
     */
    @Override
    public ControllerInterface getController() {
        return controller;
    }

    /**
     * Getter of the controller
     * @return the controller
     */
    public static ControllerInterface getControllerStatic() {
        return controller;
    }

    /** Sets the controller
     * @param controller the controller to set
     */
    public static void setControllerStatic(VirtualController controller) {
        GUI.controller = controller;
    }

    /**
     * Gets the light game
     * @return the light game
     */
    public static LightGame getLightGame(){
        return lightGame;
    }

    /**
     * Gets the light lobby
     * @return the light lobby
     */
    public static LightLobby getLobby(){
        return lobby;
    }

    /**
     * Gets the light lobby list
     * @return the light lobby list
     */
    public static LightLobbyList getLobbyList(){
        return lobbyList;
    }

    /**
     * Gets the log memory
     * @return the log memory
     */
    public static LogMemory getLogMemory() {
        return logMemory;
    }

    /**
     * Gets the property that contains the current state of the GUI
     * @return the property that contains the current state of the GUI
     */
    public static EnumProperty<StateGUI> getStateProperty() {
        return stateProperty;
    }

    /**
     * The main method. It launches the GUI
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

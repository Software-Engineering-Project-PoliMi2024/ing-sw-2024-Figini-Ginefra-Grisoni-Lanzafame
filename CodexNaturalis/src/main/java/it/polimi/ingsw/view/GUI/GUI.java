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
import it.polimi.ingsw.view.GUI.Controllers.ConnectionFormControllerGUI;
import it.polimi.ingsw.view.GUI.Controllers.LoginFormControllerGUI;
import it.polimi.ingsw.view.ViewState;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.Arrays;

public class GUI extends Application implements ActualView {
    static private VirtualController controller;
    private static final LightLobbyList lobbyList = new LightLobbyList();

    private static final LightGame lightGame = new LightGame();

    private static LogMemory logMemory = new LogMemory();

    private Stage primaryStage;
    private Root currentRoot;

    private final ImageView logoCenter = new ImageView(CardMuseumGUI.logoCenter);
    private final ImageView logoCircle = new ImageView(CardMuseumGUI.logoCircle);
    private final ImageView logoBackground = new ImageView(CardMuseumGUI.logoBackground);

    private final StackPane logoStack = new StackPane(logoBackground, logoCircle, logoCenter);

    private final AnchorPane stackRoot = new AnchorPane();

    public void run() {
        launch();
    }

    private void updateLogoSize(){
        double logoSize = Math.max(stackRoot.getWidth(), stackRoot.getHeight());

        logoCenter.setFitWidth(logoSize);
        logoCenter.setFitHeight(logoSize);

        logoCircle.setFitWidth(logoSize);
        logoCircle.setFitHeight(logoSize);

        logoBackground.setFitWidth(logoSize);
        logoBackground.setFitHeight(logoSize);
    }

    @Override
    public void start(Stage primaryStage) {
        //stackRoot.setAlignment(Pos.CENTER);
//
//        double logoWidth = stackRoot.getWidth();
//        double logoHeight = logoWidth;
//
//        logoCenter.setFitWidth(logoWidth);
//        logoCenter.setFitHeight(logoHeight);
//
//        logoCircle.setFitWidth(logoWidth);
//        logoCircle.setFitHeight(logoHeight);
//
//        logoBackground.setFitWidth(logoWidth);
//        logoBackground.setFitHeight(logoHeight);

        //Update width by listening to the stage width
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> this.updateLogoSize());

        //Update width by listening to the stage height
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> this.updateLogoSize());


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
        primaryStage.setScene(new Scene(stackRoot, 800, 600));

        AnchorPane.setTopAnchor(stackRoot, 0.0);
        AnchorPane.setBottomAnchor(stackRoot, 0.0);
        AnchorPane.setLeftAnchor(stackRoot, 0.0);
        AnchorPane.setRightAnchor(stackRoot, 0.0);

        this.updateLogoSize();

        //set stackRoot background and style
        //stackRoot.setStyle("-fx-background-color: #1e1f22;");
        transitionTo(StateGUI.SERVER_CONNECTION);
        //transitionTo(StateGUI.JOIN_LOBBY);
        //transitionTo(StateGUI.IDLE);
    }

    private void setRoot(Root root){
        if(currentRoot == null){
            stackRoot.getChildren().add(root.getRoot());
            currentRoot = root;
            primaryStage.show();
            return;
        }

        AnchorPane.setTopAnchor(logoStack, 0.0);
        AnchorPane.setBottomAnchor(logoStack, 0.0);
        AnchorPane.setLeftAnchor(logoStack, 0.0);
        AnchorPane.setRightAnchor(logoStack, 0.0);


        logoBackground.setScaleX(0);
        logoBackground.setScaleY(0);

        logoCenter.setScaleX(0);
        logoCenter.setScaleY(0);

        logoCircle.setScaleX(0);
        logoCircle.setScaleY(0);

        stackRoot.getChildren().addAll(root.getRoot(), logoStack);

        Timeline timeline = new Timeline();

        KeyValue oldRootOpacityStart = new KeyValue(currentRoot.getRoot().opacityProperty(), 1, Interpolator.EASE_BOTH);
        KeyValue oldRootOpacityGoal = new KeyValue(currentRoot.getRoot().opacityProperty(), 0, Interpolator.EASE_BOTH);

        KeyValue newRootOpacityStart = new KeyValue(root.getRoot().opacityProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue newRootOpacityGoal = new KeyValue(root.getRoot().opacityProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue centerStartX = new KeyValue(logoCenter.scaleXProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue centerGoalX = new KeyValue(logoCenter.scaleXProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue centerStartY = new KeyValue(logoCenter.scaleYProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue centerGoalY = new KeyValue(logoCenter.scaleYProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue circleStartX = new KeyValue(logoCircle.scaleXProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue circleGoalX = new KeyValue(logoCircle.scaleXProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue circleStartY = new KeyValue(logoCircle.scaleYProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue circleGoalY = new KeyValue(logoCircle.scaleYProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue backgroundStartX = new KeyValue(logoBackground.scaleXProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue backgroundGoalX = new KeyValue(logoBackground.scaleXProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue backgroundStartY = new KeyValue(logoBackground.scaleYProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue backgroundGoalY = new KeyValue(logoBackground.scaleYProperty(), 1, Interpolator.EASE_BOTH);


        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), centerStartX),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration), centerGoalX),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 4 * Configs.swapAnimationDelay + Configs.swapAnimationPause), centerGoalX),
                new KeyFrame(Duration.millis(2*Configs.swapAnimationDuration + 4 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), centerStartX),

                new KeyFrame(Duration.millis(0), centerStartY),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration), centerGoalY),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 4 * Configs.swapAnimationDelay + Configs.swapAnimationPause), centerGoalY),
                new KeyFrame(Duration.millis(2*Configs.swapAnimationDuration + 4 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), centerStartY),


                new KeyFrame(Duration.millis(Configs.swapAnimationDelay), circleStartX),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + Configs.swapAnimationDelay), circleGoalX),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 3 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), circleGoalX),
                new KeyFrame(Duration.millis(2 * Configs.swapAnimationDuration + 3 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), circleStartX),

                new KeyFrame(Duration.millis(Configs.swapAnimationDelay), circleStartY),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + Configs.swapAnimationDelay), circleGoalY),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), circleGoalY),
                new KeyFrame(Duration.millis(2 * Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), circleStartY),


                new KeyFrame(Duration.millis( 2 * Configs.swapAnimationDelay), backgroundStartX),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay), backgroundGoalX),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), backgroundGoalX),
                new KeyFrame(Duration.millis(2 * Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), backgroundStartX),


                new KeyFrame(Duration.millis( 2 * Configs.swapAnimationDelay), backgroundStartY),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay), backgroundGoalY),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), backgroundGoalY),
                new KeyFrame(Duration.millis(2 * Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay+ Configs.swapAnimationPause), backgroundStartY),

                new KeyFrame(Duration.millis(0), newRootOpacityStart),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay - 1), newRootOpacityStart),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay), newRootOpacityGoal),

                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay - 1), oldRootOpacityStart),
                new KeyFrame(Duration.millis(Configs.swapAnimationDuration + 2 * Configs.swapAnimationDelay), oldRootOpacityGoal)
        );



        timeline.setOnFinished(e -> {
            stackRoot.getChildren().clear();
            stackRoot.getChildren().add(root.getRoot());
            currentRoot = root;
        });

        timeline.play();

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
        LogErr.display(logMsg);
    }

    @Override
    public void logOthers(String logMsg) throws RemoteException {
        logMemory.addLog(logMsg);
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

    public static LogMemory getLogMemory() {
        return logMemory;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

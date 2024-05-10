package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualRMI.VirtualControllerRMI;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ApplicationGUI extends Application implements ViewInterface {
    private Stage primaryStage;
    private VirtualController controller;

    public void run() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Starting GUI: " + this);
        controller = new VirtualControllerRMI();
        ControllerGUI.setController(controller);
        ControllerGUI.setView(this);
        this.primaryStage = primaryStage;
        transitionTo(StateGUI.SERVER_CONNECTION);
    }

    public void transitionTo(StateGUI state) {
        Platform.runLater(() -> {
                primaryStage.setScene(state.getScene());
                primaryStage.show();
        });
    }

    @Override
    public void setState(ViewState state) throws RemoteException {

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
}

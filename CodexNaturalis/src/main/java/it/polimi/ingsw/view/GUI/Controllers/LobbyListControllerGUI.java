package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.GUI.Components.PreGame.LobbyListJoinGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyListControllerGUI implements Initializable {
    @FXML
    AnchorPane joinLobby;
    @FXML
    AnchorPane createGame;
    @FXML
    private TextField lobbyToCreateName;
    @FXML
    private Slider playerNumber;

    private final LobbyListJoinGUI joinLobbyDisplay = new LobbyListJoinGUI();

    public void createLobby() {
        String lobbyName = lobbyToCreateName.getText();
        if(!lobbyName.isEmpty())
            try {
                GUI.getControllerStatic().createLobby(lobbyName,(int)playerNumber.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinLobby.getChildren().addAll(joinLobbyDisplay.getLobbyListDisplay());
    }
}

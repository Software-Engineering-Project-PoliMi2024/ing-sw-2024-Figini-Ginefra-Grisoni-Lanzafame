package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyListControllerGUI implements Initializable {

    @FXML
    private TextField lobbyToCreateName;
    @FXML
    private Slider playerNumber;
    @FXML
    private ListView<LightLobby> lobbyListDisplay;
    @FXML
    private Label lobbyNameToJoin;

    public void createLobby() {
        String lobbyName = lobbyToCreateName.getText();
        if(!lobbyName.isEmpty())
            try {
                GUI.getControllerStatic().createLobby(lobbyName,(int)playerNumber.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    public void joinLobby(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyListDisplay.setCellFactory(param -> new ListCell<LightLobby>(){
            @Override
            protected void updateItem(LightLobby lobby, boolean empty) {
                super.updateItem(lobby, empty);

                if (empty || lobby == null) {
                    setText(null);
                } else {
                    setText(lobby.name());
                }
            }
        });
        lobbyListDisplay.setItems(GUI.getLobbyList().bind());
    }
}

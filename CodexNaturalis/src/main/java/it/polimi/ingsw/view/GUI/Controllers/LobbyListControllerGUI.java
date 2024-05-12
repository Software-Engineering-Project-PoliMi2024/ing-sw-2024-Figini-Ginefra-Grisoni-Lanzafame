package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ActualView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyListControllerGUI{
    private static ActualView view;
    private static LightLobbyList lobbyList;

    @FXML
    private TextField lobbyToCreateName;
    @FXML
    private Slider playerNumber;

    public static void setView(ActualView view) {
        LobbyListControllerGUI.view = view;
    }

    public static void setLobbyList(LightLobbyList lobbyList) {
        LobbyListControllerGUI.lobbyList = lobbyList;
    }

    public void createLobby() {
        String lobbyName = lobbyToCreateName.getText();
        if(!lobbyName.isEmpty())
            try {
                view.getController().createLobby(lobbyName,(int)playerNumber.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
}

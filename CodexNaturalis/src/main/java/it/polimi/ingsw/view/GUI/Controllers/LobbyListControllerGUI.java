package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.PreGame.LobbyListJoinGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LobbyListControllerGUI implements Initializable {
    @FXML
    AnchorPane joinLobby;
    @FXML
    private TextField lobbyToCreateName;
    @FXML
    private HBox characterContainer;
    private int nPlayers = 2;
    private final LobbyListJoinGUI joinLobbyDisplay = new LobbyListJoinGUI();

    public void createLobby() {
        String lobbyName = lobbyToCreateName.getText();
        if(!lobbyName.isEmpty())
            try {
                GUI.getControllerStatic().createLobby(lobbyName, nPlayers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinLobby.getChildren().addAll(joinLobbyDisplay.getLobbyListDisplay());

        for(int i = 0; i < Resource.values().length; i++) {
            Resource resource = Resource.values()[i];
            ImageView character = new ImageView(AssetsGUI.loadCharacter(resource));
            character.setPreserveRatio(true);
            character.setFitHeight(100);

            if(i >= 2)
                character.setOpacity(0.5);

            final int target = (i>=2) ? i : 1;

            character.setOnMouseClicked(e -> {
                try {
                    this.nPlayers = target + 1;
                    for (int j = 0; j < characterContainer.getChildren().size(); j++) {
                        ImageView characterView = (ImageView) characterContainer.getChildren().get(j);
                        if (j <= target) {
                            characterView.setOpacity(1);
                        } else {
                            characterView.setOpacity(0.5);
                        }
                    }
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });

            characterContainer.getChildren().add(character);
        };
    }
}

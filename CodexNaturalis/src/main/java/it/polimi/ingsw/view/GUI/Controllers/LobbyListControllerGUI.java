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

/**
 * This class is the controller for the lobby list SceneBuilder scene.

 */
public class LobbyListControllerGUI implements Initializable {
    /** The container of the lobby list.*/
    @FXML
    AnchorPane joinLobby;
    /** The text field where the user can insert the name of the lobby to create.*/
    @FXML
    private TextField lobbyToCreateName;
    /** The button to create the lobby.*/
    @FXML
    private HBox characterContainer;
    @FXML
    private HBox agentsContainer;
    /** The number of players in the lobby to create.*/
    private int nPlayers = 2;
    private int nAgents = 0;
    /** The lobby list display.*/
    private final LobbyListJoinGUI joinLobbyDisplay = new LobbyListJoinGUI();
    private final Spinner<Integer> nAgenstsSpinner = new Spinner<>(0, 3, 0);

    /**
     * Creates a new LobbyListControllerGUI and attaches the lobby list display to the controller.
     */
    public void createLobby() {
        String lobbyName = lobbyToCreateName.getText();
        if(!lobbyName.isEmpty() && nPlayers + nAgents >= 2 && nPlayers + nAgents <= 4)
            try {
                GUI.getControllerStatic().createLobby(lobbyName, nPlayers, nAgents);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    private void adjustNAgents() {
        if(this.nAgents + this.nPlayers >= 5) {
            this.nAgents = 4 - this.nPlayers;
            nAgenstsSpinner.getValueFactory().setValue(this.nAgents);
        }
    }

    /**
     * This method is called when the controller is initialized.
     * It adds the lobby list display to the container.
     * It also adds the character selection to the container.
     * @param url the url
     * @param resourceBundle the resource bundle
     */
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

            final int target = i;

            character.setOnMouseClicked(e -> {
                try {
                    this.nPlayers = target + 1;
                    adjustNAgents();
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

        nAgenstsSpinner.setEditable(true);
        nAgenstsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.nAgents = newValue;
            adjustNAgents();
        });
        agentsContainer.getChildren().add(nAgenstsSpinner);
    }
}

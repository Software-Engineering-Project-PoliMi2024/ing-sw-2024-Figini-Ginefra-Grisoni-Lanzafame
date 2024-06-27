package it.polimi.ingsw.view.GUI.Components.PreGame;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * This class represents the GUI component responsible for housing the lobby.

 */
public class LobbyGUI implements Observer {
    /** The list of users in the lobby */
    private final ListView<String> userInLobby = new ListView<>();
    /** The label that shows the number of players in the lobby */
    private final Label playerInLobbyCount = new Label();
    /** The container that contains all the components */
    private final VBox lobbyLayout = new VBox();

    /**
     * Creates a new LobbyGUI.
     */
    public LobbyGUI(){
        GUI.getLobby().attach(this);

        Label lobbyNameToJoin = new Label();
        lobbyNameToJoin.setText(GUI.getLobby().name());
        Button leaveButton = new Button();
        leaveButton.setText("leave lobby");
        leaveButton.getStyleClass().add("danger");

        leaveButton.setOnAction(e->leaveLobby());

        int numberOfMaxPlayers = GUI.getLobby().numberMaxPlayer();
        int numberOfPlayerInLobby = GUI.getLobby().nicknames().size();
        playerInLobbyCount.setText(numberOfPlayerInLobby + "/" + numberOfMaxPlayers);
        playerInLobbyCount.setPadding(new javafx.geometry.Insets(0, 0, 20, 0));

        userInLobby.getStyleClass().add("lobbyListStyle");
        userInLobby.getStyleClass().add("bordersCodexStyle");
        userInLobby.setPrefHeight(200);
        userInLobby.setMaxHeight(200);
        userInLobby.setMaxWidth(200);


        lobbyLayout.getChildren().addAll(lobbyNameToJoin, playerInLobbyCount, userInLobby, leaveButton);

        lobbyLayout.setSpacing(25);
        lobbyLayout.setAlignment(Pos.CENTER);

        AnchorPane.setRightAnchor(lobbyLayout, 10.0);
        AnchorPane.setBottomAnchor(lobbyLayout, 10.0);
        AnchorPane.setLeftAnchor(lobbyLayout, 10.0);
        AnchorPane.setTopAnchor(lobbyLayout, 10.0);
    }

    /**
     * Updates the component when called by the observed.
     * It clears all the users in the lobby and then adds them again to match the light model
     */
    @Override
    public void update() {
        userInLobby.getItems().clear();
        userInLobby.getItems().addAll(GUI.getLobby().nicknames());
        playerInLobbyCount.setText(GUI.getLobby().nicknames().size() + "/" + GUI.getLobby().numberMaxPlayer());
    }

    /**
     * Leaves the lobby.
     */
    public void leaveLobby() {
        try {
            GUI.getControllerStatic().leaveLobby();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the lobby display.
     * @return the lobby display.
     */
    public Node getLobbyDisplay() {
        return lobbyLayout;
    }
}

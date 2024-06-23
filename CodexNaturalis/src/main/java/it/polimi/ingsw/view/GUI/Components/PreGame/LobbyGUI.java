package it.polimi.ingsw.view.GUI.Components.PreGame;

import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class LobbyGUI implements Observer {
    private final ListView<String> userInLobby = new ListView<>();
    private final Label playerInLobbyCount = new Label();
    private final Label lobbyNameToJoin = new Label();
    private final Button leaveButton = new Button();
    private final VBox lobbyLayout = new VBox();

    public LobbyGUI(){
        GUI.getLobby().attach(this);

        lobbyNameToJoin.setText(GUI.getLobby().name());
        leaveButton.setText("leave lobby");
        leaveButton.getStyleClass().add("danger");

        leaveButton.setOnAction(e->leaveLobby());

        int numberOfMaxPlayers = GUI.getLobby().numberMaxPlayer();
        int numberOfPlayerInLobby = GUI.getLobby().nicknames().size();
        playerInLobbyCount.setText(numberOfPlayerInLobby + "/" + numberOfMaxPlayers);
        playerInLobbyCount.setPadding(new javafx.geometry.Insets(0, 0, 20, 0));

        userInLobby.getStyleClass().add("bordersCodexStyle");
        userInLobby.setPrefHeight(200);
        userInLobby.setMaxHeight(200);
        userInLobby.setMaxWidth(200);


        lobbyLayout.getChildren().addAll(lobbyNameToJoin, playerInLobbyCount, userInLobby, leaveButton);

        lobbyLayout.setSpacing(10);
        lobbyLayout.setAlignment(Pos.CENTER);

        AnchorPane.setRightAnchor(lobbyLayout, 10.0);
        AnchorPane.setBottomAnchor(lobbyLayout, 10.0);
        AnchorPane.setLeftAnchor(lobbyLayout, 10.0);
        AnchorPane.setTopAnchor(lobbyLayout, 10.0);
    }

    @Override
    public void update() {
        userInLobby.getItems().clear();
        userInLobby.getItems().addAll(GUI.getLobby().nicknames());
        playerInLobbyCount.setText(GUI.getLobby().nicknames().size() + "/" + GUI.getLobby().numberMaxPlayer());
    }

    public void leaveLobby() {
        try {
            GUI.getControllerStatic().leaveLobby();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Node getLobbyDisplay() {
        return lobbyLayout;
    }
}

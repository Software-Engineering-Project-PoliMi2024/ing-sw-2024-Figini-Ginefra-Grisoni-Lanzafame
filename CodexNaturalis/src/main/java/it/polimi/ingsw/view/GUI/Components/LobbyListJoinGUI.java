package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyListJoinGUI implements Observer {
    private final ListView<LightLobby> lobbyListDisplay = new ListView<>();
    private final Label lobbyNameToJoin = new Label();
    private final Button joinButton = new Button();
    private final VBox joinLobbyLayout = new VBox();

    public LobbyListJoinGUI(){
        GUI.getLobbyList().attach(this);

        lobbyNameToJoin.setText("Select a lobby to join");
        joinButton.setText("Join");

        lobbyListDisplay.setCellFactory(param -> listViewLobbyCellFactory());
        lobbyListDisplay.getSelectionModel().selectedItemProperty().addListener(linkLabelToSelection());
        joinButton.setOnAction(e->joinLobby());

        HBox buttonAndLabelLayout = new HBox(joinButton, lobbyNameToJoin);
        buttonAndLabelLayout.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        buttonAndLabelLayout.setSpacing(10);
        buttonAndLabelLayout.setAlignment(Pos.CENTER);
        joinLobbyLayout.getChildren().addAll(lobbyListDisplay, buttonAndLabelLayout);

        joinLobbyLayout.setSpacing(10);
        joinLobbyLayout.setAlignment(Pos.CENTER);

        AnchorPane.setRightAnchor(joinLobbyLayout, 10.0);
        AnchorPane.setBottomAnchor(joinLobbyLayout, 10.0);
        AnchorPane.setLeftAnchor(joinLobbyLayout, 10.0);
        AnchorPane.setTopAnchor(joinLobbyLayout, 10.0);
    }

    @Override
    public void update() {
        for(LightLobby lobby : GUI.getLobbyList().getLobbies()){
            if(!lobbyListDisplay.getItems().contains(lobby)){
                lobbyListDisplay.getItems().add(lobby);
            }
        }
        lobbyListDisplay.getItems().removeIf(lobby -> !GUI.getLobbyList().getLobbies().contains(lobby));
    }

    public ListCell<LightLobby> listViewLobbyCellFactory() {
        ListCell<LightLobby> cellFactory =  new ListCell<LightLobby>(){
            @Override
            protected void updateItem(LightLobby lobby, boolean empty) {
                super.updateItem(lobby, empty);
                if (empty || lobby == null) {
                    setText(null);
                } else {
                    setText(lobby.name());
                }
            }
        };

        return cellFactory;
    }

    public ChangeListener<LightLobby> linkLabelToSelection(){
        return new ChangeListener<LightLobby>() {
            @Override
            public void changed(ObservableValue<? extends LightLobby> observableValue, LightLobby lobby, LightLobby t1) {
                LightLobby selectedLobby = lobbyListDisplay.getSelectionModel().getSelectedItem();
                if(selectedLobby != null)
                    lobbyNameToJoin.setText(selectedLobby.name());
                else
                    lobbyNameToJoin.setText("Select a lobby to join");
            }
        };
    }

    public void joinLobby(){
        LightLobby selectedLobby = lobbyListDisplay.getSelectionModel().getSelectedItem();
        if(selectedLobby != null){
            try {
                GUI.getControllerStatic().joinLobby(selectedLobby.name());
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public Node getLobbyListDisplay() {
        return joinLobbyLayout;
    }
}

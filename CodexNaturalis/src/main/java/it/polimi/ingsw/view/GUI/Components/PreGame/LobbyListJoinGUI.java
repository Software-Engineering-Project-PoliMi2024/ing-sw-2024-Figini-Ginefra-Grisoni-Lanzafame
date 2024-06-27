package it.polimi.ingsw.view.GUI.Components.PreGame;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * This class represents the GUI component responsible for housing the lobby list.
 */
public class LobbyListJoinGUI implements Observer {
    /** The list of lobbies */
    private final ListView<LightLobby> lobbyListDisplay = new ListView<>();
    /** The button to join a lobby */
    private final Button joinButton = new Button();
    /** The container that contains all the components */
    private final VBox joinLobbyLayout = new VBox();

    /**
     * Creates a new LobbyListJoinGUI.
     */
    public LobbyListJoinGUI(){
        GUI.getLobbyList().attach(this);

        Label lobbyNameToJoin = new Label();
        lobbyNameToJoin.setText("Select a lobby to join");
        joinButton.setText("Join");

        lobbyListDisplay.setCellFactory(param -> listViewLobbyCellFactory());
        lobbyListDisplay.getSelectionModel().selectedItemProperty().addListener(linkLabelToSelection());
        lobbyListDisplay.setStyle(lobbyListDisplay.getStyle() +  "-fx-background-radius: 20;");

        joinButton.setOnAction(e->joinLobby());
        joinButton.getStyleClass().add("accent");

        VBox buttonAndLabelLayout = new VBox(lobbyNameToJoin, joinButton);
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

    /**
     * Updates the component based on the current state of the model. This method is called by the observer.
     * It updates the list of lobbies by adding the new ones and removing the ones that are not present in the model.
     */
    @Override
    public void update() {
        for(LightLobby lobby : GUI.getLobbyList().getLobbies()){
            if(!lobbyListDisplay.getItems().contains(lobby)){
                lobbyListDisplay.getItems().add(lobby);
            }
        }
        lobbyListDisplay.getItems().removeIf(lobby -> !GUI.getLobbyList().getLobbies().contains(lobby));
    }

    /**
     * Returns a cell factory for the list view of lobbies.
     * Each cell will display the name of the lobby.
     * @return a cell factory for the list view of lobbies
     */
    public ListCell<LightLobby> listViewLobbyCellFactory() {
        ListCell<LightLobby> cellFactory =  new ListCell<LightLobby>(){
            @Override
            protected void updateItem(LightLobby lobby, boolean empty) {
                super.updateItem(lobby, empty);
                if (empty || lobby == null) {
                    setText(null);
                    getStyleClass().remove("non-empty-list-cell");
                } else {
                    setText(lobby.name());
                    getStyleClass().add("non-empty-list-cell");

                }
            }
        };
        return cellFactory;
    }

    /**
     * Returns a change listener that links the label to the selection of the list view.
     * @return a change listener that links the label to the selection of the list view
     */
    public ChangeListener<LightLobby> linkLabelToSelection(){
        return new ChangeListener<LightLobby>() {
            @Override
            public void changed(ObservableValue<? extends LightLobby> observableValue, LightLobby lobby, LightLobby t1) {
                LightLobby selectedLobby = lobbyListDisplay.getSelectionModel().getSelectedItem();
                if(selectedLobby != null)
                    joinButton.setText("Join " + selectedLobby.name());
                else
                    joinButton.setText("Join");
            }
        };
    }

    /**
     * Joins the selected lobby.
     */
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

    /**
     * Returns the lobby list display.
     * @return the lobby list display
     */
    public Node getLobbyListDisplay() {
        return joinLobbyLayout;
    }
}

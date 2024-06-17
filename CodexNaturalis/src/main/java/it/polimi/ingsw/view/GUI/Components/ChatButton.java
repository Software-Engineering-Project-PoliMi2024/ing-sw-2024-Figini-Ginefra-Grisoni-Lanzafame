package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;


public class ChatButton implements Observer {

    private Button chatButton;
    private ImageView chatIcon = new ImageView(AssetsGUI.chatIcon);

    public void addThisTo(AnchorPane parent) {
        chatButton = new Button();
        chatButton.setStyle("-fx-background-color: transparent;");
        chatButton.prefHeightProperty().bind(parent.heightProperty().multiply(0.1));
        chatButton.prefWidthProperty().bind(parent.widthProperty().multiply(0.1));

        chatIcon.fitHeightProperty().bind(chatButton.heightProperty().multiply(0.8));
        chatIcon.fitWidthProperty().bind(chatButton.widthProperty().multiply(0.8));
        chatIcon.setPreserveRatio(true);
        chatButton.setGraphic(chatIcon);

        AnchorPane.setBottomAnchor(chatButton, 20.0);
        AnchorPane.setRightAnchor(chatButton, 20.0);
        parent.getChildren().add(chatButton);

        chatButton.setOnAction(event -> showChat());
    }

    private void showChat() {

    }

    @Override
    public void update() {
        if(!GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()) {
            chatIcon.setImage(AssetsGUI.unreadChatIcon);
        }

    }

    public void attachToChat() {
        GUI.getLightGame().getLightGameParty().getLightChat().attach(this);
    }
}

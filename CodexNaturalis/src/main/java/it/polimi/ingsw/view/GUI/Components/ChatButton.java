package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;


public class ChatButton implements Observer {

    private final Button chatButton = new Button();
    private final ImageView chatIcon = new ImageView(AssetsGUI.chatIcon);

    private ChatDisplay chatDisplay;

    public void addThisTo(AnchorPane parent) {
        chatDisplay = new ChatDisplay(parent);

        chatButton.setStyle("-fx-background-color: transparent;");
        chatButton.maxHeightProperty().bind(parent.heightProperty().multiply(0.1));
        chatButton.maxWidthProperty().bind(parent.widthProperty().multiply(0.1));

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
        chatIcon.setImage(AssetsGUI.chatIcon);
        chatDisplay.open();
    }

    @Override
    public void update() {
        if(chatDisplay.getDisplayedMessages().isEmpty() && !GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()) {
            chatIcon.setImage(AssetsGUI.unreadChatIcon); //edge case for a mid-game join
        }else if(!chatDisplay.getDisplayedMessages().getLast().getSender().equals(GUI.getLightGame().getLightGameParty().getYourName())) {
            chatIcon.setImage(AssetsGUI.unreadChatIcon); //show unread icon only if the last message was NOT sent by the player
        }
        //update the chat display with the new messages only if there is a new first message otherwise, the "Game" welcome message will be removed
        if(!GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()){
            chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
        }
    }

    public void attachToChat() {
        GUI.getLightGame().getLightGameParty().getLightChat().attach(this);
    }
}

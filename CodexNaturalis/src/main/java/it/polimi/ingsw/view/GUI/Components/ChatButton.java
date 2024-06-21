package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public class ChatButton implements Observer {

    private final Button chatButton = new Button();
    private final ImageView book = new ImageView(AssetsGUI.book);
    private final ImageView flower = new ImageView(AssetsGUI.flower);
    private FadeTransition unreadAnimation;
    private ChatDisplay chatDisplay;

    public void addThisTo(AnchorPane parent) {
        chatDisplay = new ChatDisplay(parent);

        chatButton.setStyle("-fx-background-color: transparent;");
        chatButton.maxHeightProperty().bind(parent.heightProperty().multiply(0.2));
        chatButton.maxWidthProperty().bind(parent.widthProperty().multiply(0.2));

        book.fitWidthProperty().bind(chatButton.widthProperty().multiply(0.8));
        book.fitHeightProperty().bind(chatButton.heightProperty().multiply(0.8));
        book.setPreserveRatio(true);
        flower.fitWidthProperty().bind(chatButton.widthProperty().multiply(0.8));
        flower.fitHeightProperty().bind(chatButton.heightProperty().multiply(0.8));
        flower.setPreserveRatio(true);

        StackPane chatIcon = new StackPane(book, flower);
        chatButton.setGraphic(chatIcon);

        AnchorPane.setBottomAnchor(chatButton, 20.0);
        AnchorPane.setRightAnchor(chatButton, 20.0);
        parent.getChildren().add(chatButton);

        createAnimation();

        chatButton.setOnAction(event -> showChat());
    }

    private void showChat() {
        stopUnreadAnimation();
        chatDisplay.open();
    }

    @Override
    public void update() {
        if(chatDisplay.getDisplayedMessages().isEmpty() && !GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()) {
           playUnreadAnimation(); //edge case for a mid-game join
        }else if(!chatDisplay.getDisplayedMessages().getLast().getSender().equals(GUI.getLightGame().getLightGameParty().getYourName())) {
            playUnreadAnimation(); //show unread icon only if the last message was NOT sent by the player
        }
        //update the chat display with the new messages only if there is a new first message otherwise, the "Game" welcome message will be removed
        if(!GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()){
            chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
        }
    }

    public void attachToChat() {
        GUI.getLightGame().getLightGameParty().getLightChat().attach(this);
    }

    private void createAnimation(){
        unreadAnimation = new FadeTransition(Duration.millis(GUIConfigs.flowerAnimationDuration), flower);
        unreadAnimation.setFromValue(1.0);
        unreadAnimation.setToValue(0.0);
        unreadAnimation.setCycleCount(FadeTransition.INDEFINITE);
        unreadAnimation.setAutoReverse(true);
    }

    public void playUnreadAnimation(){
        unreadAnimation.play();
    }

    public void stopUnreadAnimation(){
        unreadAnimation.stop();
        flower.setOpacity(1.0);
    }
}

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
        flower.setOpacity(0.0);

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
        if(newGameJoin()){
            unreadAnimation.play();
        }else if (midGameJoin()){
            //This introduces a bug.
            //Because when the first message is actually sent, the animation for the sending player will continue to play
            unreadAnimation.play();
            chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
        }else{ // normal message update
            chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
            if(unreadMessage()){
                unreadAnimation.play();
            }
        }
    }

    /**
     @return true if the last message received is not from the player
     */
    private boolean unreadMessage() {
        return !GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().getLast().getSender().equals(
                GUI.getLightGame().getLightGameParty().getYourName());
    }

    private boolean newGameJoin(){
        return GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty();
    }

    private boolean midGameJoin(){
        return (chatDisplay.getDisplayedMessages().getFirst().getSender().equals("Game") &&
                !GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty());
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
        flower.setOpacity(0.0);
    }
}

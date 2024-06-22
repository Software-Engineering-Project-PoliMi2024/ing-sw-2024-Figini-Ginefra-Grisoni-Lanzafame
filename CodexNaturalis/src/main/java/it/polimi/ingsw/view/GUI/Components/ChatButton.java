package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.utilities.Pair;
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
import java.util.HashMap;
import java.util.Map;


public class ChatButton implements Observer {

    private final Button chatButton = new Button();
    private final ImageView book = new ImageView(AssetsGUI.book);
    private final ImageView flower = new ImageView(AssetsGUI.flower);
    private FadeTransition unreadAnimation;
    private ChatDisplay chatDisplay;

    private final Map<String, Pair<PawnColors, Boolean>> chatMembers = new HashMap<>();
    private String currentPlayer = "";

    public ChatButton() {
        book.maxHeight(16);
        book.maxWidth(16);
        book.fitWidthProperty().bind(chatButton.widthProperty().multiply(0.8));
        book.fitHeightProperty().bind(chatButton.heightProperty().multiply(0.8));
        book.setPreserveRatio(true);

        flower.maxHeight(16);
        flower.maxWidth(16);
        flower.fitWidthProperty().bind(chatButton.widthProperty().multiply(0.5));
        flower.fitHeightProperty().bind(chatButton.heightProperty().multiply(0.5));
        flower.setPreserveRatio(true);
        flower.setOpacity(0.0);

        createAnimation();
    }

    public void addThisTo(AnchorPane parent) {
        chatDisplay = new ChatDisplay(parent);

        chatButton.setStyle("-fx-background-color: transparent;");
        chatButton.maxHeightProperty().set(16);
        chatButton.maxWidthProperty().set(16);

        StackPane chatIcon = new StackPane(book, flower);
        chatIcon.setMaxHeight(16);
        chatIcon.setMaxWidth(16);
        chatButton.setGraphic(chatIcon);

        createAnimation();

        chatButton.setOnAction(event -> showChat());
    }

    public Button getChatButton() {
        return chatButton;
    }

    private void showChat() {
        stopUnreadAnimation();
        chatDisplay.open();
    }

    @Override
    public void update() {
        if(isUpdatePlayerColor()){
            System.out.println("updatePlayerColor");
            updatePlayerColor();
        }else if(isUpdateRemovePlayer()) {
            System.out.println("removePlayer");
            removePlayer();
        }else if(isUpdateActivePlayer()){
                System.out.println("activePlayer");
                activePlayer();
        }else if(isUpdateInactivePlayer()) {
            System.out.println("inactivePlayer");
            inactivePlayer();
        }else if(isUpdateCurrentPlayer()){
            System.out.println( "currentPlayer");
            currentPlayer();
        }else{
            System.out.println("updateMessages");
            updateMessages();
        }
    }

    private boolean isUpdatePlayerColor(){
        boolean changed = false;
        for(String player : chatMembers.keySet()){
            if(chatMembers.get(player).first() != GUI.getLightGame().getLightGameParty().getPlayerColor(player)){
                changed = true;
                break;
            }
        }
        return changed;
    }

    private void updatePlayerColor(){
        for(String player : chatMembers.keySet()){
            chatMembers.put(player, new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(player), chatMembers.get(player).second()));
        }
        reloadMessages();
    }

    private boolean isUpdateRemovePlayer(){
        boolean removed = false;
        for(String player : chatMembers.keySet()){
            if(!GUI.getLightGame().getLightGameParty().getPlayerActiveList().containsKey(player)){
                removed = true;
                break;
            }
        }
        return removed;
    }

    private void removePlayer(){
        for(String player : chatMembers.keySet()){
            if(!GUI.getLightGame().getLightGameParty().getPlayerActiveList().containsKey(player)){
                chatMembers.remove(player);
                chatDisplay.removeReceiver(player);
                break;
            }
        }
        reloadMessages();
    }

    private boolean isUpdateInactivePlayer(){
        boolean inactive = false;
        for(String player : chatMembers.keySet()){
            if(!GUI.getLightGame().getLightGameParty().getPlayerActiveList().get(player) && chatMembers.get(player).second()){
                inactive = true;
                break;
            }
        }
        return inactive;
    }

    private void inactivePlayer(){
        for(String player : chatMembers.keySet()){
            if(!GUI.getLightGame().getLightGameParty().getPlayerActiveList().get(player) && chatMembers.get(player).second()){
                chatMembers.put(player, new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(player), false));
                chatDisplay.removeReceiver(player);
                break;
            }
        }
        reloadMessages();
    }

    private boolean isUpdateActivePlayer(){
        boolean active = false;
        for(String player : GUI.getLightGame().getLightGameParty().getPlayerActiveList().keySet()){
            if(!chatMembers.containsKey(player) || GUI.getLightGame().getLightGameParty().getPlayerActiveList().get(player) && !chatMembers.get(player).second()){
                active = true;
                break;
            }
        }
        return active;
    }

    private void activePlayer(){
        for(String player : GUI.getLightGame().getLightGameParty().getPlayerActiveList().keySet()){
            if(player.equals(GUI.getLightGame().getLightGameParty().getYourName())){
                if(!chatMembers.containsKey(GUI.getLightGame().getLightGameParty().getYourName())){
                    System.out.println("I'm the joiner");
                    chatMembers.put(GUI.getLightGame().getLightGameParty().getYourName(), new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(GUI.getLightGame().getLightGameParty().getYourName()), true));
                    chatDisplay.addReceiver(player);
                    unreadAnimation.play();
                    if(!GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()){
                        chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
                    }
                }
            }else{
                System.out.println(player + " is the joiner");
                chatDisplay.addReceiver(player);
                chatMembers.put(player, new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(player), true));
                this.reloadMessages();
            }
        }
    }

    private boolean isUpdateCurrentPlayer(){
        return !currentPlayer.equals(GUI.getLightGame().getLightGameParty().getCurrentPlayer());
    }

    private void currentPlayer(){
        currentPlayer = GUI.getLightGame().getCurrentPlayer();
        reloadMessages();
    }

    private void updateMessages(){
        if(unreadMessage()){
            unreadAnimation.play();
        }
        chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
    }

    public void reloadMessages(){
        if(!GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()){
            chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
        }
    }
    /**
     @return true if the last message received is not from the player
     */
    private boolean unreadMessage() {
        return !GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().getLast().getSender().equals(
                GUI.getLightGame().getLightGameParty().getYourName());
    }

    private void createAnimation(){
        unreadAnimation = new FadeTransition(Duration.millis(GUIConfigs.flowerAnimationDuration), flower);
        unreadAnimation.setFromValue(1.0);
        unreadAnimation.setToValue(0.0);
        unreadAnimation.setCycleCount(FadeTransition.INDEFINITE);
        unreadAnimation.setAutoReverse(true);
    }

    public void stopUnreadAnimation(){
        unreadAnimation.stop();
        flower.setOpacity(0.0);
    }

    public void attach(){
        GUI.getLightGame().getLightGameParty().getLightChat().attach(this);
        GUI.getLightGame().getLightGameParty().attach(this);
    }
}

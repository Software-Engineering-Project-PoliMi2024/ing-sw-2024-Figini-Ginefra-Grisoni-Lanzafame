package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.utils.designPatterns.Observer;
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

/**
 * This class represents the GUI component that handles the chat button.
 */
public class ChatButton implements Observer {
    /** The button that opens the chat */
    private final Button chatButton = new Button();
    /** The book icon */
    private final ImageView book = new ImageView(AssetsGUI.book);
    /** The flower icon */
    private final ImageView flower = new ImageView(AssetsGUI.flower);
    /** The animation used to show unread messages */
    private FadeTransition unreadAnimation;
    /** The chat display */
    private ChatDisplay chatDisplay;

    /** The map of chat members */
    private final Map<String, Pair<PawnColors, Boolean>> chatMembers = new HashMap<>();
    /** The current player */
    private String currentPlayer = "";

    /** Creates a new ChatButton */
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
    /**
     * Adds the chat button to the parent.
     * @param parent the parent to add the chat button to.
     */
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

    /**
     * Gets the chat button.
     * @return the chat button.
     */
    public Button getChatButton() {
        return chatButton;
    }

    /**
     * Shows the chat by opening the chat display.
     */
    private void showChat() {
        stopUnreadAnimation();
        chatDisplay.open();
    }

    /**
     * Updates the chat display based on the current state of the light model. This method is called by the observed.
     */
    @Override
    public void update() {
        if(isUpdatePlayerColor()){
            updatePlayerColor();
        }else if(isUpdateRemovePlayer()) {
            removePlayer();
        }else if(isUpdateActivePlayer()){
            activePlayer();
        }else if(isUpdateInactivePlayer()) {
            inactivePlayer();
        }else if(isUpdateCurrentPlayer()){
            currentPlayer();
        }else{
            updateMessages();
        }
    }

    /**
     * Checks if the player color has changed.
     * @return true if the player color has changed.
     */
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

    /**
     * Updates the player color.
     */
    private void updatePlayerColor(){
        for(String player : chatMembers.keySet()){
            chatMembers.put(player, new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(player), chatMembers.get(player).second()));
        }
        reloadMessages();
    }

    /**
     * Checks if a player has been removed.
     * @return true if a player has been removed.
     */
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

    /**
     * Removes a player.
     */
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

    /**
     * Checks if an inactive player has been updated.
     * @return true if an inactive player has been updated.
     */
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

    /**
     * Inactivates the offline players by setting their label to strike-through.
     */
    private void inactivePlayer(){
        for(String player : chatMembers.keySet()){
            if(!GUI.getLightGame().getLightGameParty().getPlayerActiveList().get(player) && chatMembers.get(player).second()){
                chatMembers.put(player, new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(player), false));
                break;
            }
        }
        reloadMessages();
    }

    /**
     * Checks if the active player has been updated.
     * @return true if the active player has been updated.
     */
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

    /**
     * Activates the active players by removing the strike-through from their label.
     */
    private void activePlayer(){
        for(String player : GUI.getLightGame().getLightGameParty().getPlayerActiveList().keySet()){
            if(player.equals(GUI.getLightGame().getLightGameParty().getYourName())){
                if(!chatMembers.containsKey(GUI.getLightGame().getLightGameParty().getYourName())){
                    chatMembers.put(GUI.getLightGame().getLightGameParty().getYourName(), new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(GUI.getLightGame().getLightGameParty().getYourName()), true));
                    chatDisplay.addReceiver(player);
                    unreadAnimation.play();
                    if(!GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory().isEmpty()){
                        chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
                    }
                }
            }else{
                chatDisplay.addReceiver(player);
                chatMembers.put(player, new Pair<>(GUI.getLightGame().getLightGameParty().getPlayerColor(player), true));
                this.reloadMessages();
            }
        }
    }

    /**
     * Checks if the current player has been updated.
     * @return true if the current player has been updated.
     */
    private boolean isUpdateCurrentPlayer(){
        return !currentPlayer.equals(GUI.getLightGame().getLightGameParty().getCurrentPlayer());
    }

    /**
     * Updates the current player.
     */
    private void currentPlayer(){
        currentPlayer = GUI.getLightGame().getCurrentPlayer();
        reloadMessages();
    }

    /**
     * Updates the chat display with the current messages.
     */
    private void updateMessages(){
        if(unreadMessage() && !chatDisplay.isOpen()){
            unreadAnimation.play();
        }
        chatDisplay.updatedMessages(GUI.getLightGame().getLightGameParty().getLightChat().getChatHistory());
    }

    /**
     * Adds a player to the chat members.
     */
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

    /**
     * Creates the animation used to show unread messages.
     */
    private void createAnimation(){
        unreadAnimation = new FadeTransition(Duration.millis(GUIConfigs.flowerAnimationDuration), flower);
        unreadAnimation.setFromValue(1.0);
        unreadAnimation.setToValue(0.0);
        unreadAnimation.setCycleCount(FadeTransition.INDEFINITE);
        unreadAnimation.setAutoReverse(true);
    }

    /**
     * Stops the animation used to show unread messages.
     */
    public void stopUnreadAnimation(){
        unreadAnimation.stop();
        flower.setOpacity(0.0);
    }

    /**
     * Attaches the chat button to the chat display and the light game party.

     */
    public void attach(){
        GUI.getLightGame().getLightGameParty().getLightChat().attach(this);
        GUI.getLightGame().getLightGameParty().attach(this);
    }
}

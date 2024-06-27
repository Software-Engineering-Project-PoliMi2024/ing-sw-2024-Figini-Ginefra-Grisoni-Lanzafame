package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnsGui;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

/**
 * This class represents the GUI component responsible for housing the chat.

 */
public class ChatDisplay {
    /** The popup that contains the chat */
    private final PopUp chatPopUp;
    /** The list of messages */
    private final ListView<ChatMessage> messages = new ListView<>();
    /** The choice box to select the receiver of the message */
    private ChoiceBox<String> receiverChoice;
    /** The text field to write the message */
    private TextField sendMessageField;

    /**
     * Creates a new ChatDisplay.
     * @param parent The parent of the chat
     */
    public ChatDisplay(AnchorPane parent) {
        chatPopUp = new PopUp(parent);
        messages.setCellFactory(param -> listViewChatMessageCellFactory());
        messages.getSelectionModel().selectedItemProperty().addListener(linkMessage());
        messages.setStyle(messages.getStyle() +  "-fx-background-radius: 20;");

        VBox popUpFiller = new VBox();
        popUpFiller.setSpacing(20.0);
        AnchorPane.setTopAnchor(popUpFiller, 20.0);
        AnchorPane.setBottomAnchor(popUpFiller, 20.0);
        AnchorPane.setLeftAnchor(popUpFiller, 20.0);
        AnchorPane.setRightAnchor(popUpFiller, 20.0);

        //Create a container for received messages which will contain the ListView
        HBox receivedMessagesContainer = new HBox();
        receivedMessagesContainer.setAlignment(Pos.CENTER);
        receivedMessagesContainer.getChildren().add(messages);;
        VBox.setVgrow(receivedMessagesContainer, Priority.ALWAYS);
        HBox.setHgrow(messages, Priority.ALWAYS);

        //Create a container for sending messages which will contain the TextField, Button and the ChoiceBox
        HBox sendMessageContainer = new HBox();
        sendMessageContainer.setSpacing(20.0);
        sendMessageContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(sendMessageContainer, Priority.ALWAYS);

        HBox sendingOptionContainer = initializeSendingOptionContainer(new HBox());
        sendMessageContainer.getChildren().addAll(initializeSendMessageField(), sendingOptionContainer);

        popUpFiller.getChildren().addAll(receivedMessagesContainer, sendMessageContainer);
        chatPopUp.getContent().getChildren().add(popUpFiller);

        //Add welcome message that will be removed when the first message is received
        messages.getItems().add(new ChatMessage("Game", "Welcome to the chat!"));
    }

    /**
     * Creates a new ListCell for the chat messages.
     * @return The ListCell
     */
    public ListCell<ChatMessage> listViewChatMessageCellFactory() {
        ListCell<ChatMessage> cellFactory = new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage chatMessage, boolean empty) {
                super.updateItem(chatMessage, empty);
                if (empty || chatMessage == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().remove("non-empty-list-cell");
                } else {
                    HBox messageContainer = new HBox();
                    messageContainer.setAlignment(Pos.CENTER_LEFT);

                    Label message = labelDecorator(chatMessage);

                    messageContainer.getChildren().add(message);
                    setGraphic(messageContainer);

                    getStyleClass().add("non-empty-list-cell");
                }
            }
        };
        return cellFactory;
    }

    /**
     * Decorates the label with the message.
     * @param message The message to decorate
     * @return The decorated label
     */
    private Label labelDecorator(ChatMessage message){
        Label labelMessage = new Label();
        labelStyleDecorator(message, labelMessage);
        TextFlow textFlow;
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC) {
            textFlow = new TextFlow(senderDecorator(message.getSender()), new Text(": " + message.getMessage()));
        }else{ //Private message
            textFlow = new TextFlow(senderDecorator(message.getSender()), new Text(" to: "), receiverDecorator(message.getReceiver()), new Text(": " + message.getMessage()));
        }
        labelMessage.setGraphic(textFlow);
        return labelMessage;
    }

    /**
     * Decorates the sender of the message.
     * @param sender The sender of the message
     * @return The decorated sender
     */
    private Text senderDecorator(String sender){
        Text senderText = new Text();
        if(sender.equals("Game")){
            senderText.setStyle("-fx-font-weight: bold");
        }else {
            playerTextDecorator(sender, senderText);
        }
        if(sender.equals(GUI.getLightGame().getLightGameParty().getYourName())){
            senderText.setText("You");
        }else {
            senderText.setText(sender);
        }
        return senderText;
    }

    /**
     * Decorates the receiver of the message.
     * @param receiver The receiver of the message
     * @return The decorated receiver
     */
    private Text receiverDecorator(String receiver){
        Text receiverText = new Text();
        playerTextDecorator(receiver, receiverText);
        if(receiver.equals(GUI.getLightGame().getLightGameParty().getYourName())){
            receiverText.setText("you");
        }else{
            receiverText.setText(receiver);
        }
        return receiverText;
    }

    /**
     * Decorates the label with the message.
     * @param message The message to decorate
     * @param labelToEdit The label to edit
     */
    private void labelStyleDecorator(ChatMessage message, Label labelToEdit){
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PRIVATE){
            labelToEdit.setStyle("-fx-font-style: italic");
        }
    }

    /**
     * Decorates the player text.
     * @param player The player to decorate
     * @param text The text to decorate
     */
    private void playerTextDecorator(String player, Text text){
        if(!GUI.getLightGame().getLightGameParty().getPlayerActiveMap().containsKey(player)){
            text.setFill(Color.GRAY);
        } else { // Only if the player is still in the game, the text will be bold
            text.setFill(getUserColor(player));
            StringBuilder style = new StringBuilder("-fx-font-weight: bold;");

            if(GUI.getLightGame().getLightGameParty().getCurrentPlayer().equals(player)){
                style.append("-fx-underline: true;");
            } else {
                style.append("-fx-underline: false;");
            }

            if(GUI.getLightGame().getLightGameParty().getPlayerActiveMap().get(player)) {
                style.append("-fx-strikethrough: false;");
            } else {
                style.append("-fx-strikethrough: true;");
            }

            text.setStyle(style.toString());
        }
    }

    /**
     * Updates the messages.
     * @param updatedMessages The updated messages
     */
    public void updatedMessages(List<ChatMessage> updatedMessages) {
        this.messages.getItems().removeAll(messages.getItems());
        this.messages.getItems().addAll(updatedMessages);
    }

    /**
     * Opens the chat.
     */
    public void open() {
        chatPopUp.open();
    }

    /**
     * Closes the chat.
     */
    private void sendMessage(){
        String sender = GUI.getLightGame().getLightGameParty().getYourName();
        String receiver = receiverChoice.getValue();
        String message = sendMessageField.getText();
        if(message.isEmpty()){
            return;
        }
        try{
            if(receiver.equals(publicMsg.EVERYONE.toString())) {
                GUI.getControllerStatic().sendChatMessage(new ChatMessage(sender, message));
            }else{
                GUI.getControllerStatic().sendChatMessage(new ChatMessage(sender, message, receiver));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        sendMessageField.clear();
    }

    /**
     * Links the message to the chat.
     * @return The listener
     */
    public ChangeListener<ChatMessage> linkMessage(){
        return new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends ChatMessage> observableValue, ChatMessage chatMessage, ChatMessage t1) {
                ChatMessage selectedMessage = messages.getSelectionModel().getSelectedItem();
                if(selectedMessage != null && !selectedMessage.getSender().equals("Game")
                        && !selectedMessage.getSender().equals(GUI.getLightGame().getLightGameParty().getYourName())){
                    sendMessageField.setPromptText("Write a message to: " + selectedMessage.getSender());
                    receiverChoice.setValue(selectedMessage.getSender());
                }else{
                    receiverChoice.setValue(publicMsg.EVERYONE.toString());
                    sendMessageField.setPromptText("Write a message to: " + receiverChoice.getValue());
                }
            }
        };
    }

    /**
     * Links the choice box to the chat.
     * @return The listener
     */
    public ChangeListener<String> linkChoice(){
        return new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                sendMessageField.setPromptText("Write a message to: " + receiverChoice.getValue());
            }
        };
    }

    /**
     * Initializes the receiver choice.
     * @return The choice box
     */
    private ChoiceBox<String> initializeReceiverChoice(){
        receiverChoice = new ChoiceBox<>();
        receiverChoice.getItems().add(publicMsg.EVERYONE.toString());
        //todo you need to load all Player not only the active ones
        for(String player : GUI.getLightGame().getLightGameParty().getPlayerActiveMap().keySet()){
            if(!player.equals(GUI.getLightGame().getLightGameParty().getYourName())){
                receiverChoice.getItems().add(player);
            }
        }
        receiverChoice.setValue(publicMsg.EVERYONE.toString());
        receiverChoice.getSelectionModel().selectedItemProperty().addListener(linkChoice());
        return receiverChoice;
    }

    /**
     * Initializes the send button.
     * @return The send button
     */
    private Button initializeSendButton(){
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());
        sendButton.getStyleClass().add("accent");
        return sendButton;
    }

    /**
     * Initializes the send message field.
     * @return The send message field
     */
    private TextField initializeSendMessageField(){
        sendMessageField = new TextField();
        sendMessageField.setPromptText("Write a message to: " + receiverChoice.getValue());
        HBox.setHgrow(sendMessageField, Priority.ALWAYS);

        sendMessageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });

        return sendMessageField;
    }

    /**
     * Initializes the sending option container.
     * @param sendingOptionContainer The container to initialize
     * @return The initialized container
     */
    private HBox initializeSendingOptionContainer(HBox sendingOptionContainer){
        sendingOptionContainer.setSpacing(10.0);
        sendingOptionContainer.setAlignment(Pos.CENTER);
        sendingOptionContainer.getChildren().addAll(initializeReceiverChoice(), initializeSendButton());
        return sendingOptionContainer;
    }

    /**
     * Gets the color of the user.
     * @param user The user
     * @return The color of the user
     */
    private Color getUserColor(String user){
        PawnColors pawnColor = GUI.getLightGame().getLightGameParty().getPlayerColor(user);
        if(pawnColor == null){
            return Color.BLACK;
        }else{
            return PawnsGui.getColor(pawnColor);
        }
    }

    /**
     * Adds a receiver to the chat.
     * @param player The player to add
     */
    public void addReceiver(String player) {
        if(!receiverChoice.getItems().contains(player) && !player.equals(GUI.getLightGame().getLightGameParty().getYourName())){
            receiverChoice.getItems().add(player);
        }
    }

    /**
     * Removes a receiver from the chat.
     * @param player The player to remove
     */
    public void removeReceiver(String player) {
        receiverChoice.getItems().remove(player);
    }

    /**
     * @return true if the chat is open, false otherwise
     */
    public boolean isOpen() {
        return chatPopUp.isOpen();
    }

    /**
     * Closes the chat.
     */
    private enum publicMsg {
        EVERYONE;

        @Override
        public String toString() {
            return "Everyone";
        }
    }

}

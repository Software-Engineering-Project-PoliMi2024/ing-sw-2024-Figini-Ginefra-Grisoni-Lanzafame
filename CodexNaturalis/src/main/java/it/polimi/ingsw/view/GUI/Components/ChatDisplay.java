package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChatDisplay {
    private final PopUp chatPopUp;
    private final ListView<ChatMessage> messages = new ListView<>();

    private ChoiceBox<String> receiverChoice;
    private Button sendButton;
    private TextField sendMessageField;

    public ChatDisplay(AnchorPane parent) {
        chatPopUp = new PopUp(parent);
        messages.setCellFactory(param -> listViewChatMessageCellFactory());
        messages.getSelectionModel().selectedItemProperty().addListener(linkMessage());

        VBox popUpFiller = new VBox();
        popUpFiller.setSpacing(20.0);
        AnchorPane.setTopAnchor(popUpFiller, 20.0);
        AnchorPane.setBottomAnchor(popUpFiller, 20.0);
        AnchorPane.setLeftAnchor(popUpFiller, 20.0);
        AnchorPane.setRightAnchor(popUpFiller, 20.0);

        //Create a container for received messages which will contain the ListView
        HBox receivedMessagesContainer = new HBox();
        receivedMessagesContainer.setAlignment(Pos.CENTER);
        receivedMessagesContainer.getChildren().add(messages);
        VBox.setVgrow(receivedMessagesContainer, Priority.ALWAYS);

        //Create a container for sending messages which will contain the TextField, Button and the ChoiceBox
        VBox sendMessageContainer = new VBox();
        sendMessageContainer.setSpacing(20.0);
        sendMessageContainer.setAlignment(Pos.CENTER);
        VBox sendingOptionContainer = initializeSendingOptionContainer(new VBox());
        sendMessageContainer.getChildren().addAll(initializeSendMessageField(), sendingOptionContainer);

        popUpFiller.getChildren().addAll(receivedMessagesContainer, sendMessageContainer);
        chatPopUp.getContent().getChildren().add(popUpFiller);

        //Add welcome message that will be removed when the first message is received
        messages.getItems().add(new ChatMessage("Game", "Welcome to the chat!"));
    }

    public ListCell<ChatMessage> listViewChatMessageCellFactory() {
        ListCell<ChatMessage> cellFactory = new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage chatMessage, boolean empty) {
                super.updateItem(chatMessage, empty);
                if (empty || chatMessage == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox messageContainer = new HBox();
                    messageContainer.setAlignment(Pos.CENTER);
                    Label sender = new Label(chatMessage.getSender() + ": ");
                    Label message = new Label(chatMessage.getMessage());
                    Label receiver = new Label();
                    if(chatMessage.getPrivacy() == ChatMessage.MessagePrivacy.PRIVATE){
                        receiver.setText(" -> " + chatMessage.getReceiver());
                        messageContainer.getChildren().addAll(sender, message, receiver);
                    }else{
                        messageContainer.getChildren().addAll(sender, message);
                    }
                    setGraphic(messageContainer);
                }
            }
        };
        return cellFactory;
    }

    public void updatedMessages(List<ChatMessage> updatedMessages) {
        this.messages.getItems().removeAll(messages.getItems());
        this.messages.getItems().addAll(updatedMessages);
    }

    public void open() {
        chatPopUp.open();
    }

    private void sendMessage(){
        String sender = GUI.getLightGame().getLightGameParty().getYourName();
        String message = sendMessageField.getText();
        String receiver = receiverChoice.getValue();
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

    public ChangeListener<String> linkChoice(){
        return new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                sendMessageField.setPromptText("Write a message to: " + receiverChoice.getValue());
            }
        };
    }

    private ChoiceBox<String> initializeReceiverChoice(){
        receiverChoice = new ChoiceBox<>();
        receiverChoice.getItems().add(publicMsg.EVERYONE.toString());
        for(String player : GUI.getLightGame().getLightGameParty().getPlayerActiveList().keySet()){
            if(!player.equals(GUI.getLightGame().getLightGameParty().getYourName())){
                receiverChoice.getItems().add(player);
            }
        }
        receiverChoice.setValue(publicMsg.EVERYONE.toString());
        receiverChoice.getSelectionModel().selectedItemProperty().addListener(linkChoice());
        return receiverChoice;
    }

    private Button initializeSendButton(){
        //TODO add icon
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());
        return sendButton;
    }

    private TextField initializeSendMessageField(){
        sendMessageField = new TextField();
        sendMessageField.setPromptText("Write a message to: " + receiverChoice.getValue());
        return sendMessageField;
    }

    private VBox initializeSendingOptionContainer(VBox sendingOptionContainer){
        sendingOptionContainer.setSpacing(10.0);
        sendingOptionContainer.setAlignment(Pos.CENTER);
        sendingOptionContainer.getChildren().addAll(initializeReceiverChoice(), initializeSendButton());
        return sendingOptionContainer;
    }

    private enum publicMsg {
        EVERYONE;
        @Override
        public String toString() {
            return "Everyone";
        }
    }
}

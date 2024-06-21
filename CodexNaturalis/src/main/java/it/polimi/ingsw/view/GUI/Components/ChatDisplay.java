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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class ChatDisplay {
    private final PopUp chatPopUp;
    private final ListView<ChatMessage> messages = new ListView<>();

    private ChoiceBox<String> receiverChoice;
    private TextField sendMessageField;

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

    private Text senderDecorator(String sender){
        Text senderText = new Text();
        if(sender.equals(GUI.getLightGame().getLightGameParty().getYourName())){
            senderText.setText("You");
        }else{
            senderText.setText(sender);
        }
        senderText.setFill(getUserColor(sender));
        senderText.setStyle("-fx-font-weight: bold");
        return senderText;
    }

    private Text receiverDecorator(String receiver){
        Text receiverText = new Text();
        if(receiver.equals(GUI.getLightGame().getLightGameParty().getYourName())){
            receiverText.setText("you");
        }else{
            receiverText.setText(receiver);
        }
        receiverText.setFill(getUserColor(receiver));
        receiverText.setStyle("-fx-font-weight: bold");
        return receiverText;
    }

    private void labelStyleDecorator(ChatMessage message, Label labelToEdit){
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PRIVATE){
            labelToEdit.setStyle("-fx-font-style: italic");
        }
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
        //todo you need to load all Player not only the active ones
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
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());
        sendButton.getStyleClass().add("accent");
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

    private Color getUserColor(String user){
        PawnColors pawnColor = GUI.getLightGame().getLightGameParty().getPlayerColor(user);
        if(pawnColor == null){
            return Color.BLACK;
        }else{
            return PawnsGui.getColor(pawnColor);
        }
    }

    private enum publicMsg {
        EVERYONE;

        @Override
        public String toString() {
            return "Everyone";
        }
    }

    public List<ChatMessage> getDisplayedMessages(){
        return new ArrayList<>(messages.getItems());
    }
}

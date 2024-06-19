package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class ChatDisplay {
    private PopUp chatPopUp;
    private final ListView<ChatMessage> messages = new ListView<>();

    public ChatDisplay(AnchorPane parent) {
        chatPopUp = new PopUp(parent);
        messages.setCellFactory(param -> listViewChatMessageCellFactory());
        chatPopUp.getContent().getChildren().add(messages);

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
                    HBox hBox = new HBox();
                    Label sender = new Label(chatMessage.getSender() + ": ");
                    Label message = new Label(chatMessage.getMessage());
                    Label receiver = new Label();
                    if(chatMessage.getPrivacy() == ChatMessage.MessagePrivacy.PRIVATE){
                        receiver.setText(" -> " + chatMessage.getReceiver());
                        hBox.getChildren().addAll(sender, message, receiver);
                    }else{
                        hBox.getChildren().addAll(sender, message);
                    }
                    setGraphic(hBox);
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
}

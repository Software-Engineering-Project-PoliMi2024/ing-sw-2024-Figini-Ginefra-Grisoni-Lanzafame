package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.model.playerReleted.ChatMessage;

public class ChatMsg extends ClientMsg{
    private final ChatMessage message;

    public ChatMsg(ChatMessage message) {
        this.message = message;
    }
    public ChatMessage getMessage() {
        return message;
    }
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().sendChatMessage(message);
    }
}

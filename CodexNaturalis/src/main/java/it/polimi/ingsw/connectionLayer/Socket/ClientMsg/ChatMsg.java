package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.model.playerReleted.ChatMessage;

/**
 * Message that contains a chat message
 */
public class ChatMsg extends ClientMsg{
    /**
     * The chat message
     */
    private final ChatMessage message;

    /**
     * Constructor
     * @param message the chat message
     */
    public ChatMsg(ChatMessage message) {
        this.message = message;
    }

    /**
     * Call the controller for sending the chat message to the other players
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().sendChatMessage(message);
    }
}

package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.ServerHandler;
import it.polimi.ingsw.controller.socket.messages.NetworkMessage;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

import java.io.IOException;
import java.util.UUID;

public abstract class AnswerMsg extends ServerMsg {
    UUID parentIdentifier;


    /**
     * Initializes the answer message.
     * @param parent The CommandMsg being answered.
     */
    public AnswerMsg(ActionMsg parent)
    {
        this.parentIdentifier = parent.getIdentifier();
    }


    /**
     * Returns the identifier of the message being answered.
     * @return The UUID of the answered message.
     */
    public UUID getParentIdentifier()
    {
        return parentIdentifier;
    }


}

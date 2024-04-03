package it.polimi.ingsw.controller.socket.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class NetworkMessage implements Serializable {
    UUID identifier = UUID.randomUUID();


    /**
     * Unique identifier for the message.
     * Allows matching a message with its response.
     * @return The identifier
     */
    public UUID getIdentifier()
    {
        return identifier;
    }
}

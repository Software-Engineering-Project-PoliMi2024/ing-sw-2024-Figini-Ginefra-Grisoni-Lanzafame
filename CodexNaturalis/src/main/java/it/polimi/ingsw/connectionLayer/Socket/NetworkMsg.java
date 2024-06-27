package it.polimi.ingsw.connectionLayer.Socket;

import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract class that represents a socketMessage
 */
public abstract class NetworkMsg implements Serializable {
        /**
         * Unique identifier for the message.
         * Allows matching a message with its response.
         */
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

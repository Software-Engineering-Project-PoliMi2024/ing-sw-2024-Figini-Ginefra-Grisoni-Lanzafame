package it.polimi.ingsw.connectionLayer.Socket;

import java.io.Serializable;
import java.util.UUID;

public abstract class NetworkMsg implements Serializable {
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

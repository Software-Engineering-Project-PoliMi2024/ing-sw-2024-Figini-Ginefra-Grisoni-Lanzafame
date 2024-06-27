package it.polimi.ingsw.model.playerReleted;

import java.io.Serializable;

/**
 * Class that represents a chat message
 */
public class ChatMessage implements Serializable {
    /** the actual message */
    private final String message;
    /** the sender of the message */
    private final String sender;
    /** the receiver of the message */
    private final String receiver;
    /** the privacy level of the message */
    private final MessagePrivacy privacy;
    /** the possible privacy levels of the message */
    public enum MessagePrivacy {
        PUBLIC,
        PRIVATE
    }

    /**
     * Create a new private chat message
     * @param sender the sender of the message
     * @param message the message
     * @param receiver the receiver of the message
     */
    public ChatMessage(String sender, String message, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.privacy = MessagePrivacy.PRIVATE;
    }

    /**
     * Create a new public chat message
     * @param sender the sender of the message
     * @param message the message
     */
    public ChatMessage(String sender, String message) {
        this.message = message;
        this.sender = sender;
        this.receiver = null;
        this.privacy = MessagePrivacy.PUBLIC;
    }
    /** @return the receiver of the message */
    public String getReceiver() {
        return receiver;
    }

    /** @return the message */
    public String getMessage() {
        return message;
    }

    /** @return the sender of the message */
    public String getSender() {
        return sender;
    }

    /** @return the privacy level of the message */
    public MessagePrivacy getPrivacy() {
        return privacy;
    }
}

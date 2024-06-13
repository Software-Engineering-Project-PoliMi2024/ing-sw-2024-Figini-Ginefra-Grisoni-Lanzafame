package it.polimi.ingsw.model.playerReleted;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private final String message;
    private final String sender;
    private final String receiver;
    private final MessagePrivacy privacy;
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

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public MessagePrivacy getPrivacy() {
        return privacy;
    }
}

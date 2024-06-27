package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.playerReleted.ChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to manage the chat of the game.
 * It stores the chat history of each player and allows to retrieve it.
 */
public class ChatManager implements Serializable {
    /** the chat history per each player */
    private final Map<String, List<ChatMessage>> playerChatHistories = new HashMap<>();

    /**
     * Constructor of the class.
     * @param playerNames the list of the players' nicknames
     */
    public ChatManager(List<String> playerNames) {
        for(String playerName : playerNames){
            playerChatHistories.put(playerName, new ArrayList<>());
        }
    }

    /**
     * Adds a message to the chat history of the players involved in the message.
     * @param message the message to add
     */
    public void addMessage(ChatMessage message){
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC){
            for(String playerName : playerChatHistories.keySet()){
                playerChatHistories.get(playerName).add(message);
            }
        } else {
            playerChatHistories.get(message.getReceiver()).add(message);
            playerChatHistories.get(message.getSender()).add(message);
        }
    }

    /**
     * Retrieves the chat history of a player.
     * @param nickname the nickname of the player
     * @return the chat history of the player
     */
    public List<ChatMessage> retrieveChat(String nickname){
        return playerChatHistories.get(nickname);
    }
}

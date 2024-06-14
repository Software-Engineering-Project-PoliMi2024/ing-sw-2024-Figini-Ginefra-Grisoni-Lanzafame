package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.playerReleted.ChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManager implements Serializable {
    private final Map<String, List<ChatMessage>> playerChatHistories = new HashMap<>();
    public ChatManager(List<String> playerNames) {
        for(String playerName : playerNames){
            playerChatHistories.put(playerName, new ArrayList<>());
        }
    }
    
    public void addMessage(ChatMessage message){
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC){
            for(String playerName : playerChatHistories.keySet()){
                playerChatHistories.get(playerName).add(message);
            }
        } else {
            playerChatHistories.get(message.getReceiver()).add(message);
        }
    }
}

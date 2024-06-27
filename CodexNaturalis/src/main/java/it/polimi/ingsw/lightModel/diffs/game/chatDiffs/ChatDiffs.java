package it.polimi.ingsw.lightModel.diffs.game.chatDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.ChatMessage;

import java.util.List;

/**
 * This class contain an update for the Chat
 */
public class ChatDiffs extends GameDiff {
    /** List of message to be add to the lightChat*/
    private final List<ChatMessage> messages;

    /**
     * Create a new ChatDiffs
     * @param messages List of message to be add to the lightChat
     */
    public ChatDiffs(List<ChatMessage> messages) {
        this.messages = messages;
    }

    /**
     * For each message in the list add it to the lightChat
     * @param lightGame the lightGame to which the diff is applied
     */
    @Override
    public void apply(LightGame lightGame) {
        for(ChatMessage message : messages){
            lightGame.getLightGameParty().getLightChat().addMessage(message);
        }
    }
}

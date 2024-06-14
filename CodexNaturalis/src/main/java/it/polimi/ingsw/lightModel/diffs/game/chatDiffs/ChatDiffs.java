package it.polimi.ingsw.lightModel.diffs.game.chatDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.ChatMessage;

import java.util.List;

public class ChatDiffs extends GameDiff {
    private final List<ChatMessage> messages;

    public ChatDiffs(List<ChatMessage> messages) {
        this.messages = messages;
    }
    @Override
    public void apply(LightGame lightGame) {
        for(ChatMessage message : messages){
            lightGame.getLightGameParty().getLightChat().addMessage(message);
        }
    }
}

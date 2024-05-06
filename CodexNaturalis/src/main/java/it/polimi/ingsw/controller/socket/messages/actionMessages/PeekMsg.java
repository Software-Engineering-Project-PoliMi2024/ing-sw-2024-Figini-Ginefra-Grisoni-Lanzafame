package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.PeekAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.IOException;

public class PeekMsg extends ActionMsg{
    String username;
    public PeekMsg(String username){
        super();
        this.username = username;
    }
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
       User peekedUser = socketClientHandler.getGame().getGameParty().getUsersList().stream()
               .filter(user -> user.getNickname().equals(username)).findFirst().orElse(null);
       if(peekedUser==null){
            throw new IllegalArgumentException();
       }else{
           socketClientHandler.sendServerMessage(
                   new PeekAnswerMsg(this, peekedUser.getUserCodex(),
                           peekedUser.getUserHand(), peekedUser.getNickname()));
       }
    }
}

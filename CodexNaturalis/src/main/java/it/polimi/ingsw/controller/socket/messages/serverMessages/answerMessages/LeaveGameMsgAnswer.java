package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;

import java.io.IOException;

public class LeaveGameMsgAnswer extends AnswerMsg{

    public LeaveGameMsgAnswer(ActionMsg parent){
        super(parent);
    }

    /**
     * ask for the getActiveGameList/kill the connection
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException if
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        System.out.println("Leaving the game");
        //view.transitionTo(GameList)? If we dont want to give the player the ability
        //to play multiple game at once, we should kill the connection I guess
        socketServerHandler.getClient().getController().getActiveGameList();
    }
}

package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;

import java.io.IOException;

public class LeaveLobbyAnswerMessage extends AnswerMsg{
    public LeaveLobbyAnswerMessage(ActionMsg parent)
    {
        super(parent);
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        System.out.println("Leaving lobby");

        socketServerHandler.sendActionMessage(new GetActiveGameListActionMsg());
    }
}

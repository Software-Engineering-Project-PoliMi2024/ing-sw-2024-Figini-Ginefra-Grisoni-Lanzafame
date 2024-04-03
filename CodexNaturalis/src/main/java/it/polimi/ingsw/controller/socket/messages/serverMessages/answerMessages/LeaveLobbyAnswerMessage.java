package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.ServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;

import java.io.IOException;

public class LeaveLobbyAnswerMessage extends AnswerMsg{
    public LeaveLobbyAnswerMessage(ActionMsg parent)
    {
        super(parent);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        System.out.println("Leaving lobby");

        serverHandler.sendActionMessage(new GetActiveGameListActionMsg());
    }
}

package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.ActiveGamesListAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;

public class GetActiveGameListActionMsg extends ActionMsg{

    public GetActiveGameListActionMsg()
    {
        super();
    }

    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        String[] games = socketClientHandler.getGames().getGameNames();
        socketClientHandler.sendServerMessage(new ActiveGamesListAnswerMsg(this, games));
    }
}

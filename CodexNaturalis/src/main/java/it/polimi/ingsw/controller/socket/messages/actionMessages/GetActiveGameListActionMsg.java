package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.ActiveGamesListAnswerMsg;
import it.polimi.ingsw.controller.socket.server.ClientHandler;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;

public class GetActiveGameListActionMsg extends ActionMsg{

    public GetActiveGameListActionMsg()
    {
        super();
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        String[] games = clientHandler.getGames().stream().map(Game::getName).toArray(String[]::new);
        clientHandler.sendServerMessage(new ActiveGamesListAnswerMsg(this, games));
    }
}

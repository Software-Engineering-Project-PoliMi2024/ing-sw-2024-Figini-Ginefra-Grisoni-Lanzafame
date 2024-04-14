package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.ActiveGamesListAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;

import java.io.IOException;

public class GetActiveGameListActionMsg extends ActionMsg{
    /**
     * The constructor of the class
     */
    public GetActiveGameListActionMsg()
    {
        super();
    }

    /**
     * Send an ActiveGamesListAnswerMsg with an array of all Games' names.
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        String[] games = socketClientHandler.getGames().getGameNames();
        socketClientHandler.sendServerMessage(new ActiveGamesListAnswerMsg(this, games));
    }
}

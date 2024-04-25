package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.LeaveGameMsgAnswer;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.LeaveGameNotificationMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.LeaveLobbyNotificationMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.EmptyMatchException;
import it.polimi.ingsw.model.tableReleted.UserNotFoundException;

import java.io.IOException;

public class LeaveGameMsg extends ActionMsg{
    /**
     * The constructor of the class
     */
    public LeaveGameMsg(){
        super();
    }

    /**
     * Remove the player from the current match, the observerList
     * Send a LeaveGameMsgAnswer and a LeaveGameNotificationMsg
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     * @throws RuntimeException if the match is left empty or the user is not found
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
    }
}

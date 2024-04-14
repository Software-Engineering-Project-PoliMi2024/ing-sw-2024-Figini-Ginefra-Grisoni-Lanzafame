package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.LeaveLobbyAnswerMessage;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.LeaveLobbyNotificationMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.EmptyMatchException;
import it.polimi.ingsw.model.tableReleted.UserNotFoundException;

import java.io.IOException;

public class LeaveLobbyMsg extends ActionMsg{


    /**
     * Remove the user from the game.
     * Send a LeaveLobbyAnswerMessage to the client who disconnected
     * Send a LeaveLobbyNotificationMsg every other client in the Lobby
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        socketClientHandler.getGame().getGameParty().detach(socketClientHandler);
        try {
            socketClientHandler.getGame().getGameParty().removeUser(socketClientHandler.getUser());
        } catch (EmptyMatchException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        final String nickname = socketClientHandler.getUser().getNickname();

        socketClientHandler.getGame().getGameParty().notifyObservers(new LeaveLobbyNotificationMsg(nickname));

        System.out.println(socketClientHandler.getUser().getNickname() + " left the lobby.");
        System.out.println("Active Players:" + socketClientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));

        socketClientHandler.sendServerMessage(new LeaveLobbyAnswerMessage(this));
    }
}

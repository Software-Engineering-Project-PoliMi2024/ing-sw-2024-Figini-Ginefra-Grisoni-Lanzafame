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
            String nickname = socketClientHandler.getUser().getNickname();
            String gameName = socketClientHandler.getGame().getName();

            ActionMsg.updateGameParty(socketClientHandler, gameParty -> {
                try {
                    gameParty.removeUser(socketClientHandler.getUser());
                } catch (EmptyMatchException | UserNotFoundException e) {
                    throw new RuntimeException(e);
                }
                gameParty.detach(socketClientHandler);
                gameParty.notifyObservers(new LeaveLobbyNotificationMsg(nickname));
            });
            //For the same reason in JoinGameMsg, the set of the game to be null must be outside the lambda function
            socketClientHandler.setGame(null);
            System.out.println("User " + nickname + " left the game " + gameName);
            System.out.println("Active Players:" + socketClientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));
    }
}

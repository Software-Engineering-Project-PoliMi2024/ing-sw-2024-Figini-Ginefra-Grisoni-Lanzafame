package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.LeaveLobbyAnswerMessage;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.LeaveLobbyNotificationMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.playerReleted.User;

public class LeaveLobbyMsg extends ActionMsg{
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) {
        try {
            socketClientHandler.getGame().getGameParty().detach(socketClientHandler);
            socketClientHandler.getGame().getGameParty().removeUser(socketClientHandler.getUser());

            final String nickname = socketClientHandler.getUser().getNickname();

            socketClientHandler.getGame().getGameParty().notifyObservers(new LeaveLobbyNotificationMsg(nickname));

            System.out.println(socketClientHandler.getUser().getNickname() + " left the lobby.");
            System.out.println("Active Players:" + socketClientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));

            socketClientHandler.sendServerMessage(new LeaveLobbyAnswerMessage(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

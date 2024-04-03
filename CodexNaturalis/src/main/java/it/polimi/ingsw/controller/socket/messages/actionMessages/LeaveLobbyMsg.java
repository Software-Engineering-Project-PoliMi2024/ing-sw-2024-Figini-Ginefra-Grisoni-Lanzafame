package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.LeaveLobbyAnswerMessage;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.LeaveLobbyNotificationMsg;
import it.polimi.ingsw.controller.socket.server.ClientHandler;
import it.polimi.ingsw.designPatterns.Observer.Observer;
import it.polimi.ingsw.model.playerReleted.User;

public class LeaveLobbyMsg extends ActionMsg{
    @Override
    public void processMessage(ClientHandler clientHandler) {
        try {
            clientHandler.getGame().getGameParty().detach(clientHandler);
            clientHandler.getGame().getGameParty().removeUser(clientHandler.getUser());

            final String nickname = clientHandler.getUser().getNickname();

            clientHandler.getGame().getGameParty().notifyObservers();

            System.out.println(clientHandler.getUser().getNickname() + " left the lobby.");
            System.out.println("Active Players:" + clientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));

            clientHandler.sendServerMessage(new LeaveLobbyAnswerMessage(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

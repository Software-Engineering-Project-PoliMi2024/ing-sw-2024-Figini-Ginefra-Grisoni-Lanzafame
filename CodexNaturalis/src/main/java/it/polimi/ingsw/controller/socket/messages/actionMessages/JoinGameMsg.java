package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.JoinGameAnswerMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.JoinGameNotificationMsg;
import it.polimi.ingsw.controller.socket.server.ClientHandler;
import it.polimi.ingsw.designPatterns.Observer.Observer;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.FullMatchException;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;

public class JoinGameMsg extends ActionMsg{
    private final String gameName;
    private final String nickname;

    public JoinGameMsg(String gameName, String nickname)
    {
        super();
        this.gameName = gameName;
        this.nickname = nickname;
    }

    public String getGameName()
    {
        return this.gameName;
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {
        User user = new User(nickname);
        try{
            Game targetGame = clientHandler.getGames().stream().filter(game -> game.getName().equals(gameName)).findFirst().orElse(null);
            clientHandler.setGame(targetGame);
            clientHandler.getGame().getGameParty().addUser(user);

            final String joiningNickname = nickname;

            clientHandler.getGame().getGameParty().notifyObservers();

            clientHandler.getGame().getGameParty().attach(clientHandler);

            System.out.println("User " + nickname + " joined game " + gameName);
            System.out.println("Active Players:" + clientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));

            clientHandler.setUser(user);

            clientHandler.sendServerMessage(new JoinGameAnswerMsg(this, gameName));

        } catch (FullMatchException e) {
            e.printStackTrace();
        }
    }
}

package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.JoinGameAnswerMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.JoinGameNotificationMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
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
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        try{
            Game targetGame = socketClientHandler.getGames().getGame(gameName);

            if(targetGame == null){
                System.out.println("Game " + gameName + " not found.");
                socketClientHandler.sendServerMessage(new JoinGameAnswerMsg(this, gameName, JoinGameAnswerMsg.Status.ERROR));
                return;
            }

            socketClientHandler.setGame(targetGame);
            socketClientHandler.getGame().getGameParty().addUser(socketClientHandler.getUser());

            socketClientHandler.getGame().getGameParty().notifyObservers(new JoinGameNotificationMsg(nickname));

            socketClientHandler.getGame().getGameParty().attach(socketClientHandler);

            System.out.println("User " + nickname + " joined game " + gameName);
            System.out.println("Active Players:" + socketClientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));

            socketClientHandler.sendServerMessage(new JoinGameAnswerMsg(this, gameName, JoinGameAnswerMsg.Status.OK));

        } catch (FullMatchException e) {
            e.printStackTrace();
        }
    }
}

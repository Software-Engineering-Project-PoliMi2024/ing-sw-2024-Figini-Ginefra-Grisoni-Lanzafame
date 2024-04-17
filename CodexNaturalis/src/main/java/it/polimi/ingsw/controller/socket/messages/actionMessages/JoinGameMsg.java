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

    /**
     * The constructor of the class
     * @param gameName of the game that the player want to join
     * @param nickname of the player
     */
    public JoinGameMsg(String gameName, String nickname)
    {
        super();
        this.gameName = gameName;
        this.nickname = nickname;
    }

    /**
     * @return the game that the player want to join
     */
    public String getGameName()
    {
        return this.gameName;
    }

    /**
     * Try to add the user to the gameName game. Send a JoinGameAnswerMsg with the status OK-ERROR
     * If the player is successfully added to the game, add his ClientHandler to the list of client to be notified
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws RuntimeException If an error occurs during the sending of the NOTIFICATION, such as a network failure.
     * @throws IllegalCallerException if the user is trying to enter a full game lobby
     * @throws IOException If an error occurs during the sending of the ANSWER, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        Game targetGame = socketClientHandler.getGames().getGame(gameName);
        if(targetGame==null){
            System.out.println("Game " + gameName + " not found.");
            socketClientHandler.sendServerMessage(new JoinGameAnswerMsg(this, gameName, JoinGameAnswerMsg.Status.ERROR));
        }else{
            //Using this setter is allowed because setGame is defined inside socketClientHandler witch is directly reachable from JoinGameMsg
            //Setting the game inside the lambdaFunction will cause an error, because socket.getGame() will return null
            socketClientHandler.setGame(targetGame);
        }

        ActionMsg.updateGameParty(socketClientHandler, gameParty -> {
            try {
                gameParty.addUser(socketClientHandler.getUser());
            } catch (FullMatchException e) {
                throw new IllegalCallerException(e);
            }
            gameParty.notifyObservers(new JoinGameNotificationMsg(nickname));
            gameParty.attach(socketClientHandler);
        });

        System.out.println("User " + nickname + " joined game " + gameName);
        System.out.println("Active Players:" + socketClientHandler.getGame().getGameParty().getUsersList().stream().map(User::getNickname).reduce("", (a, b) -> a + " " + b));
        socketClientHandler.sendServerMessage(new JoinGameAnswerMsg(this, gameName, JoinGameAnswerMsg.Status.OK));
    }
}

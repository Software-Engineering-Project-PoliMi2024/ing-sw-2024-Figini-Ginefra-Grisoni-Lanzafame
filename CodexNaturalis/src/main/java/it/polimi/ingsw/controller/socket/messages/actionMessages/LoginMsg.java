package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.LoginAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.IOException;

public class LoginMsg extends ActionMsg{
    private final String nickname;

    /**
     * The constructor of the clas
     * @param nickname of the player
     */
    public LoginMsg(String nickname)
    {
        super();
        this.nickname = nickname;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname()
    {
        return nickname;
    }

    /**
     * try to add the user to the Multigame by checking if is nick is unique.
     * Always respond to the user's client with an LoginAnswerMsg with a Status (OK/ERROR)
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        System.out.println("User " + nickname + " is trying to login");

        if(!socketClientHandler.getGames().addUser(nickname)) {
            socketClientHandler.sendServerMessage(new LoginAnswerMsg(this, LoginAnswerMsg.Status.ERROR));
            System.out.println("! User " + nickname + " already exists !");
        }
        else {
            socketClientHandler.sendServerMessage(new LoginAnswerMsg(this, LoginAnswerMsg.Status.OK));
            socketClientHandler.setUser(new User(nickname));
            System.out.println("User " + nickname + " logged in");
        }
    }
}

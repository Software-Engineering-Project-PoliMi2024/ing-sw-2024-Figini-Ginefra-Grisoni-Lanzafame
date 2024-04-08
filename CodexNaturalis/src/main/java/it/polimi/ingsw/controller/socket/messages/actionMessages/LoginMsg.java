package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.LoginAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.IOException;

public class LoginMsg extends ActionMsg{
    private final String nickname;

    public LoginMsg(String nickname)
    {
        super();
        this.nickname = nickname;
    }

    public String getNickname()
    {
        return nickname;
    }

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

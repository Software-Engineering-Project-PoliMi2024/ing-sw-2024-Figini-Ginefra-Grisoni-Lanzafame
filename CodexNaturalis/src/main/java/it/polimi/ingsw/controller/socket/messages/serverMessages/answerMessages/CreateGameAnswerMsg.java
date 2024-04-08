package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.JoinGameMsg;

import java.io.IOException;

public class CreateGameAnswerMsg extends AnswerMsg{

    public enum Status
    {
        OK,
        ERROR
    }

    private final Status status;
    private final String gameName;

    public CreateGameAnswerMsg(ActionMsg parent, String gameName, Status status)
    {
        super(parent);
        this.status = status;
        this.gameName = gameName;
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        System.out.println(this.gameName + " creation: " + (status == Status.OK ? "success" : "failure") + ".");


        if(status == Status.OK)
            socketServerHandler.sendActionMessage(new JoinGameMsg(this.gameName, socketServerHandler.getNickname()));
    }
}
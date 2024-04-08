package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

public class LoginAnswerMsg extends AnswerMsg {
    public enum Status
    {
        OK,
        ERROR
    }

    private final Status status;

    public LoginAnswerMsg(ActionMsg parent, Status status)
    {
        super(parent);
        this.status = status;
    }

    public Status getStatus()
    {
        return status;
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler){
        //View Update

        if(status == Status.OK) {
            System.out.println("Login successful");
            //view setup for game list
            socketServerHandler.sendActionMessage(new GetActiveGameListActionMsg());
        }
        else
            System.out.println("Login failed");
            //view setup for login

    }

}

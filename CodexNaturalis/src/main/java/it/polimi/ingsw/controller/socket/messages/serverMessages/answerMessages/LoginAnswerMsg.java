package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;

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

    /**
     * If the loginStatus is ok, transition the View to GameList and send a GetActiveGameListActionMsg
     * else, re-transition the view to Connect_Form
     * @param socketServerHandler who received an answer/notification from the server
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler){
        if(status == Status.OK) {
            System.out.println("Login successful");
            socketServerHandler.sendActionMessage(new GetActiveGameListActionMsg());
        }
        else
            System.out.println("Login failed");
            //View_TransitionTo(Connect_Form)
    }

}

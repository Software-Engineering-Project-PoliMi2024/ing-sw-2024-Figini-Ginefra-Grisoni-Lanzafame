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

    /**
     * the constructor of the class
     * @param parent The CommandMsg being answered.
     * @param gameName the name of the game that is being created
     * @param status is equal to OK if the game was being successfully added to the Multigame else is equal to ERROR
     */
    public CreateGameAnswerMsg(ActionMsg parent, String gameName, Status status)
    {
        super(parent);
        this.status = status;
        this.gameName = gameName;
    }

    /**
     * Update the view and if the game has an OK-status, send a JoinGameMsg
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //View update
        System.out.println(this.gameName + " creation: " + (status == Status.OK ? "success" : "failure") + ".");
        if(status == Status.OK)
            socketServerHandler.sendActionMessage(new JoinGameMsg(this.gameName, socketServerHandler.getNickname()));
    }
}

package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;

import java.io.IOException;

public class DisconnectAnswerMsg extends AnswerMsg{

    public DisconnectAnswerMsg(ActionMsg parent){
        super(parent);
    }

    /**
     * Update the view, stop the connection
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view kill?
        socketServerHandler.getClient().terminate();
        System.out.println(socketServerHandler.getClient().getNickname() + " Server leaved");
    }
}

package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;

import java.io.IOException;

public class LeaveLobbyAnswerMessage extends AnswerMsg{

    /**
     * The constructor of the class
     * @param parent The CommandMsg being answered.
     */
    public LeaveLobbyAnswerMessage(ActionMsg parent)
    {
        super(parent);
    }

    /**
     * update the view, send a getActiveGameListActionMsg
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        System.out.println("Leaving lobby");
        //view.tranistioTo(GameList)
        socketServerHandler.sendActionMessage(new GetActiveGameListActionMsg());
    }
}

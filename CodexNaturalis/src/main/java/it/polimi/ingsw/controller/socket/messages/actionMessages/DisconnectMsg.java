package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.DisconnectAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import java.io.IOException;

public class DisconnectMsg extends ActionMsg{
    /**
     * The constructor of the class
     */
    public DisconnectMsg(){
        super();
    }

    /**
     * Remove player username from the MultiGame obj, send a DisconnectAnswerMsg
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        String nickname = socketClientHandler.getUser().getNickname();
        System.out.println(nickname + " is trying to disconnect");
        ActionMsg.updateMultiGame(socketClientHandler, games -> games.removeUser(nickname));
        socketClientHandler.sendServerMessage(new DisconnectAnswerMsg(this));
        System.out.println(nickname + " disconnected");
    }
}

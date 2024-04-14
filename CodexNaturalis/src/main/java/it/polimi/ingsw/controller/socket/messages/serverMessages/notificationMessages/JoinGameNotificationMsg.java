package it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

public class JoinGameNotificationMsg extends ServerMsg {
    private final String nickname;

    /**
     * The constructor of the class
     * @param nickname of the player who is joining
     */
    public JoinGameNotificationMsg(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
      * Update the view
      * @param socketServerHandler who received an answer/notification from the server
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) {
        //view.update
        System.out.println(this.nickname + " joined the game. HERE");
    }
}

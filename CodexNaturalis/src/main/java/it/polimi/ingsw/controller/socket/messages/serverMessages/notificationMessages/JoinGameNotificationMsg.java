package it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages;

import it.polimi.ingsw.controller.socket.client.ServerHandler;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

public class JoinGameNotificationMsg extends ServerMsg {
    private final String nickname;

    public JoinGameNotificationMsg(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) {
        System.out.println(this.nickname + " joined the game. HERE");
    }
}

package it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

public class GamePartyNotificationMsg extends ServerMsg {
    private final String[] nicknames;

    public GamePartyNotificationMsg(String[] nicknames) {
        this.nicknames = nicknames;
    }

    public String[] getNicknames() {
        return nicknames;
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler) {
        System.out.println("Game party updated: " + String.join(", ", nicknames));
    }
}

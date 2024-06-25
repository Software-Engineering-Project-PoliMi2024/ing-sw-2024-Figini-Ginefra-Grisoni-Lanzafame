package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public class LogChatMsg extends ServerMsg{
    private final String logMsg;

    public LogChatMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().logChat(logMsg);
    }
}

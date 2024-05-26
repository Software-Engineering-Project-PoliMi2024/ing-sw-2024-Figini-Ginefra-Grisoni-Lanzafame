package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public class LogGameMsg extends ServerMsg{
    private final String logMsg;

    public LogGameMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    @Override
    public void processMsg(ServerHandler clientHandler) throws Exception {
        clientHandler.getView().logGame(logMsg);
    }
}

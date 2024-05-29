package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public class LogMsg extends ServerMsg{
    private String log;

    public LogMsg(String log) {
        this.log = log;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().log(log);
    }
}

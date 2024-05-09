package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public class LogMsg extends ServerMsg{
    private String log;

    public LogMsg(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        //todo review Generic exception of log
        serverHandler.getView().log(log);
    }
}

package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public class LogErrMsg extends ServerMsg{
    private final String logMsg;

    public LogErrMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    @Override
    public void processMsg(ServerHandler clientHandler) throws Exception {
        clientHandler.getView().logErr(logMsg);
    }
}

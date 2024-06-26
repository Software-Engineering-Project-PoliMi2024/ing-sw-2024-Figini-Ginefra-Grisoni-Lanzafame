package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

/**
 * Log chat message
 */
public class LogMsg extends ServerMsg{
    /** Message to log */
    private String log;

    /**
     * Class constructor
     * @param log Message to log
     */
    public LogMsg(String log) {
        this.log = log;
    }

    /**
     * Call the log method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().log(log);
    }
}

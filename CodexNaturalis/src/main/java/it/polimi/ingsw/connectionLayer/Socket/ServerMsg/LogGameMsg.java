package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

/**
 * Log game message class
 */
public class LogGameMsg extends ServerMsg{
    /** Message to log */
    private final String logMsg;

    /**
     * Class constructor
     * @param logMsg Message to log
     */
    public LogGameMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    /**
     * Call the logGame method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().logGame(logMsg);
    }
}
